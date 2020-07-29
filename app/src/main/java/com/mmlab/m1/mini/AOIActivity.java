package com.mmlab.m1.mini;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mmlab.m1.R;
import com.mmlab.m1.mini.adapter.LOISequenceAdapter;
import com.mmlab.m1.mini.model.LOIModel;
import com.mmlab.m1.mini.model.LOISequenceModel;
import com.mmlab.m1.mini.save_data.SaveAOISequence;
import com.mmlab.m1.mini.service.HttpAsyncTask;
import com.mmlab.m1.mini.service.TaskCompleted;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AOIActivity extends AppCompatActivity implements TaskCompleted {

    private MyApplication globalVariable;
    private RecyclerView mRecyclerView;
    private LOISequenceAdapter mAdapter;
    private ArrayList<LOISequenceModel> aoiSequenceList = new ArrayList<>();
    private LOIModel model;
    private Menu mMenu;
    private OkHttpClient client = new OkHttpClient();
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aoi);

        model = getIntent().getParcelableExtra("AOI-Content");
        globalVariable = (MyApplication) getApplicationContext();
        token = MiniActivity.getToken();
        uploadLog(MiniActivity.account_for_login , String.valueOf(MiniActivity.curr_lat) , String.valueOf(MiniActivity.curr_lng),"",model.getLOIId() , MiniActivity.uuid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(model.getLOIName());

        ExpandableTextView mDescription = (ExpandableTextView) findViewById(R.id.expand_text_view);
        mDescription.setText(model.getLOIInfo());
        TextView docentName = (TextView) findViewById(R.id.docent_name);
        //docentName.setText(model.getContributor());

//		if (model.getIdentifier().equals("docent")) {
//
//			try {
//				docentName.setText(model.getContributorDetail().getString("name"));
//				docentName.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View view) {
//						MaterialDialog materialDialog;
//						String alertMessage = "";
//						JSONObject obj = model.getContributorDetail();
//						materialDialog = new MaterialDialog.Builder(view.getContext())
//								.title("導覽員資訊")
//								.positiveText(R.string.confirm)
//								.onPositive(new MaterialDialog.SingleButtonCallback() {
//									@Override
//									public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//										// TODO
//									}
//								}).build();
//						try {
//							alertMessage += "姓名:" + obj.getString("name");
//							alertMessage += "\n電話: " + obj.getString("telphone");
//							alertMessage += "\n手機: " + obj.getString("cellphone");
//							alertMessage += "\nEmail: " + obj.getString("email");
//							alertMessage += "\n地址: " + obj.getString("user_address");
//							alertMessage += "\n社群帳號: " + obj.getString("social_id");
//							alertMessage += "\n導覽解說地區: " + obj.getString("docent_area");
//							alertMessage += "\n導覽解說使用語言: " + obj.getString("docent_language");
//							alertMessage += "\n收費標準: " + obj.getString("charge");
//							alertMessage += "\n自我介紹: " + obj.getString("introduction");
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						materialDialog.setMessage(alertMessage);
//						materialDialog.show();
//					}
//				});
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        String type = globalVariable.getType();
        String type2;
        if(type.equals("AOI-Id")) {
            type2 = "AOI-Sequence";
            String api = getResources().getString(R.string.api_aoi_seq);
            String url = api + /*"id=" +*/
                    model.getLOIId(); //+ "&did=" + globalVariable.getDeviceID()
            //+ "&appver=mini200&ulat=22.9942&ulng=120.1659&clang=" + globalVariable.getLanguage();

            Log.d("url", url);

            if (globalVariable.checkInternet()) {
               // new HttpAsyncTask(AOIActivity.this, "IP",0).execute(getResources().getString(R.string.api_clientIP));
                new HttpAsyncTask(AOIActivity.this, type2,0,token).execute(url);
            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(new LOISequenceAdapter(this, aoiSequenceList, type2, model.getIdentifier()));
        }else{
            type2 = "AOI-Sequence";
            aoiSequenceList = MiniActivity.getServer().getAOImodel().getmPOIs();
            for (int i = 0; i < aoiSequenceList.size(); ++i) {
                Log.d("id:", aoiSequenceList.get(i).getIdentifier());
                if (aoiSequenceList.get(i).getIdentifier().equals("docent")) {
                    new HttpAsyncTask(AOIActivity.this, "contributor-detail:" + Integer.toString(i), 0)
                            .execute(getResources().getString(R.string.api_docent_info) + aoiSequenceList.get(i).getContributor());
                }
            }
            mAdapter = new LOISequenceAdapter(this, aoiSequenceList, type2, model.getIdentifier(), model.getContributor());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
        }





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poi_list, menu);
        //　如果當前是群組導覽功能就除去地圖功能



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        /*if (id == R.id.narrator_info) {
            if (model.getIdentifier().equals("docent")) {
                MaterialDialog materialDialog;
                String alertMessage = "";
                JSONObject obj = model.getContributorDetail();
                materialDialog = new MaterialDialog.Builder(AOIActivity.this)
                        .title("導覽員資訊")
                        .positiveText(R.string.confirm)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                // TODO
                            }
                        }).build();
                try {
                    alertMessage += "姓名:" + obj.getString("name");
                    alertMessage += "\n電話: " + obj.getString("telphone");
                    alertMessage += "\n手機: " + obj.getString("cellphone");
                    alertMessage += "\nEmail: " + obj.getString("email");
                    alertMessage += "\n地址: " + obj.getString("user_address");
                    alertMessage += "\n社群帳號: " + obj.getString("social_id");
                    alertMessage += "\n導覽解說地區: " + obj.getString("docent_area");
                    alertMessage += "\n導覽解說使用語言: " + obj.getString("docent_language");
                    alertMessage += "\n收費標準: " + obj.getString("charge");
                    alertMessage += "\n自我介紹: " + obj.getString("introduction");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                materialDialog.setMessage(alertMessage);
                materialDialog.show();
            }
            return true;
        }*/


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String response, String type) {

        if (type.equals("IP")) {
            globalVariable.setIp(response.replaceAll("\n", ""));
            Log.d("ip", globalVariable.getIp());

        } else if (globalVariable.getIp() != null) {
            if (type.equals("AOI-Sequence")) {

                // String result = Encryption.decode(this, response, globalVariable.getIp());
                Log.d("test", response);
                //aoiSequenceList = MiniActivity.getServer().getLOImodel().getmPOIs();
                //Log.d("pois>>", "~~" + aoiSequenceList);
                SaveAOISequence saveLOISequence = new SaveAOISequence(response,"SOI");
                aoiSequenceList = saveLOISequence.getAOISequenceList();
                for (int i = 0; i < aoiSequenceList.size(); ++i) {
                    if (aoiSequenceList.get(i).getIdentifier().equals("docent")) {
                        Log.d("name=", "??" + aoiSequenceList.get(i).getContributor());
                        new HttpAsyncTask(AOIActivity.this, "contributor-detail:" + Integer.toString(i), 0)
                                .execute(getResources().getString(R.string.api_docent_info) + aoiSequenceList.get(i).getContributor());
                    }
                }
                mAdapter = new LOISequenceAdapter(this, aoiSequenceList, type, model.getIdentifier(), model.getContributor(), MiniActivity.getServer());
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(mAdapter);


                String hint = getString(R.string.find) + " " + aoiSequenceList.size() + " " + getString(R.string.sites);
                Snackbar.make(mRecyclerView, hint, Snackbar.LENGTH_SHORT).show();
            }
            else if (type.contains("contributor-detail")) {
                //String result = Encryption.decode(this, response, globalVariable.getIp());
                Log.d("result:",response);
                try {
                    JSONObject obj = new JSONObject(response);
                    int index = Integer.valueOf(type.substring("contributor-detail:".length()));

                    aoiSequenceList.get(index).setContributorDetail(obj);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    public void uploadLog(String userAccount , String ula , String ulo , String action , String clicked_poi_id , String uuid){

        //action要調
        FormBody.Builder formBody = new FormBody.Builder();
        /*formBody.add("useraccount", MiniActivity.account_for_login);
        formBody.add("ula", String.valueOf(MiniActivity.curr_lat));
        formBody.add("ulo", String.valueOf(MiniActivity.curr_lng));
        formBody.add("action", "/API/test/loi_detail/" + model.getLOIId());
        formBody.add("devid", MiniActivity.uuid);*/
        String url = "http://deh.csie.ncku.edu.tw:8080/api/v1/users/xoilog?useraccount=" +  userAccount + "&ula=" + ula + "&ulo=" + ulo + "&devid=" + uuid + "&action=" + "/API/test/aoi_detail/" +clicked_poi_id;
        Request requestt = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();
        Log.d("URL", url);
        client.newCall(requestt).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", "Failed Log");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("OKHTTP", "Success Log");
            }
        });

    }


}
