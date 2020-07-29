package com.mmlab.m1.mini.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.mmlab.m1.mini.helper.ExternalStorage;
import com.mmlab.m1.mini.network.ProxyService;
import com.mmlab.m1.mini.helper.Preset;
import com.mmlab.m1.mini.save_data.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mmlab on 2015/9/25.
 */
public class FileDownload extends AsyncTask<String, Void, Void> {

    public static final int POST_NONE = 0;
    public static final int POST_ALL = 1;
    private static final String TAG = "FileDownload";
    private static final int BUFFER_SIZE = 4096;
    private WeakReference<Service> weakReference = null;

    private int postHandle = 0;

    public FileDownload(Service service, int postHandle) {
        weakReference = new WeakReference<Service>(service);
        this.postHandle = postHandle;
    }

    protected Void doInBackground(String... strings) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        Log.d(TAG, "start file download : " + strings.length);
        for (String fileURL : strings) {
            try {
                Log.d(TAG, "file : " + fileURL);
                fileURL = fileURL.replace("moe2//", "");
                String saveFilePath = ExternalStorage.BASE_ROOT + File.separator + ExternalStorage.TARGET_DIRECTORY + File.separator + File.separator;
                Log.d(TAG, "saveFilePath : " + saveFilePath);
                int fileType = Utils.fileType(fileURL);
                switch (fileType) {
                    case ExternalStorage.TYPE_IMAGE:
                        saveFilePath = saveFilePath + Utils.urlToFilename(fileURL);
                        Log.d(TAG, "Image download");
                        break;
                    case ExternalStorage.TYPE_VIDEO:
                        saveFilePath = saveFilePath + Utils.urlToFilename(fileURL);
                        Log.d(TAG, "video download");
                        continue;
                        // break;
                    case ExternalStorage.TYPE_AUDIO:
                        saveFilePath = saveFilePath + Utils.urlToFilename(fileURL);
                        Log.d(TAG, "audio download");
                        continue;
                        // break;
                    case ExternalStorage.TYPE_NONE:
                        continue;
                    default:
                        continue;
                }

                // Get the abstract pathname of this abstract pathnames's parent
                File file = new File(saveFilePath);
                file.getParentFile().mkdirs();

                // Check whether it exists
                if (file.exists()) {
                    Log.d(TAG, "file exists : " + file.getPath());
                    continue;
                }
                URL url = new URL(fileURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "responseCode : " + "HTTP_OK");
                    if (!file.createNewFile()) continue;
                    // save current download file name and file length
                    if (weakReference.get() != null) {
                        Preset.saveFilePreferences(weakReference.get().getApplicationContext(), Utils.urlToFilename(fileURL), connection.getContentLength());
                        Log.d(TAG, "contentLength : " + connection.getContentLength());
                    }
                    Log.d(TAG, "file name : " + file.getPath() + " mediaLength : " + connection.getContentLength());


                    SharedPreferences pref = weakReference.get().getSharedPreferences("StreamingMaster", Context.MODE_PRIVATE);
                    pref.edit().putLong(Utils.urlToFilename(fileURL), connection.getContentLength()).apply();

                    // opens input stream from the HTTP connection
                    inputStream = connection.getInputStream();

                    // opens an output stream to save into file
                    outputStream = new FileOutputStream(file);

                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    String pRemoteUri = fileURL;
                    Intent intent;
                    int readSize = 0;
                    int lastReadSize = 0;
                    byte[] buf = new byte[4 * 1024];

                    while ((bytesRead = inputStream.read(buffer)) != -1) {

                        // write to file
                        try {
                            outputStream.write(buffer, 0, bytesRead);
                            readSize += bytesRead;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // 是否傳送給所有成員
                    switch (fileType) {
                        case ExternalStorage.TYPE_IMAGE:
                            if (weakReference.get() != null) {
                                Handler handler = ((ProxyService) weakReference.get()).getpHandler();
                                handler.sendMessage(handler.obtainMessage(5, saveFilePath));
                            }
                            break;
                        case ExternalStorage.TYPE_VIDEO:
                            break;
                        case ExternalStorage.TYPE_AUDIO:
                            break;
                        case ExternalStorage.TYPE_NONE:
                            break;
                        default:
                    }
                } else {
                    Log.d(TAG, "responseCode : " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
                Log.d(TAG, "FileDownload in finally clause");
            }

        }
        return null;
    }
}
