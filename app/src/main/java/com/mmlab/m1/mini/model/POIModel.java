package com.mmlab.m1.mini.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by waynewei on 2015/8/31.
 */
public class POIModel implements Parcelable {

	private String mPOIId;
	private String mPOIName;
	private String mPOIDistance;
	private String mPOISubject;
	private String mPOIType1;
	private String mPOIAddress;
	private String mPOIInfo;
	private Double mPOILat;
	private Double mPOILong;
	private String mPOIPeriod;
	private String mPOISource;
	private String mIdentifier;
	private String mContributor;
	private String mOpen;
	private ArrayList<String> mMedia;
	private ArrayList<String> mPOIKeyWords;
	private ArrayList<String> mUrl;
	private ArrayList<String> mPOIPics;
	private ArrayList<String> mPOIAudios;
	private ArrayList<String> mPOIMovies;
	private ArrayList<String> mPOIAudioTours;
	private String jsonObject;


//	public POIModel(String poiId, String poiName, String poiDistance, String poiSubject, String poiType1,
//					String poiAddress, String poiInfo, String poiLat, String poiLong,
//					String poiSource, ArrayList<String> poiKeywords, ArrayList<String> poiPics,
//					ArrayList<String> poiAudios, ArrayList<String> poiMovies) {
//		mPOIId = poiId;
//		mPOIName = poiName;
//		mPOIDistance = poiDistance;
//		mPOISubject = poiSubject;
//		mPOIType1 = poiType1;
//		mPOIAddress = poiAddress;
//		mPOIInfo = poiInfo;
//		mPOILat = poiLat;
//		mPOILong = poiLong;
//		mPOISource = poiSource;
//		mPOIKeyWords = poiKeywords;
//		mPOIPics = poiPics;
//		mPOIAudios = poiAudios;
//		mPOIMovies = poiMovies;
//	}

	public POIModel(){

	}

	protected POIModel(Parcel in) {
		mPOIId = in.readString();
		mPOIName = in.readString();
		mPOIDistance = in.readString();
		mPOISubject = in.readString();
		mPOIType1 = in.readString();
		mPOIAddress = in.readString();
		mPOIInfo = in.readString();
		mPOILat = in.readDouble();
		mPOILong = in.readDouble();
		mPOIPeriod = in.readString();
		mPOISource = in.readString();
		mIdentifier = in.readString();
		mContributor = in.readString();
		mOpen = in.readString();
		mMedia = in.createStringArrayList();
		mPOIKeyWords = in.createStringArrayList();
		mUrl = in.createStringArrayList();
		mPOIPics = in.createStringArrayList();
		mPOIAudios = in.createStringArrayList();
		mPOIMovies = in.createStringArrayList();
		mPOIAudioTours = in.createStringArrayList();
		jsonObject = in.readString();
	}
	public POIModel(POIModel p){
		mPOIId = p.getPOIId();
		mPOIName = p.getPOIName();
		mPOIDistance = p.getPOIDistance();
		mPOISubject = p.getPOISubject();
		mPOIType1 = p.getPOIType1();
		mPOIAddress = p.getPOIAddress();
		mPOIInfo = p.getPOIInfo();
		mPOILat = p.getPOILat();
		mPOILong = p.getPOILong();
		mPOIPeriod = p.getPOIPeriod();
		mPOISource = p.getPOISource();
		mIdentifier = p.getIdentifier();
		mContributor = p.getContributor();
		mOpen = p.getOpen();
		mMedia = p.getMedia();
		mPOIKeyWords = p.getPOIKeywords();
		mUrl = p.getUrl();
		mPOIPics = p.getPOIPics();
		mPOIAudios = p.getPOIAudios();
		mPOIMovies = p.getPOIPMovies();
		mPOIAudioTours = p.getPOIAudioTours();
		jsonObject = p.getJsonObject().toString();
	}

	public static final Creator<POIModel> CREATOR = new Creator<POIModel>() {
		@Override
		public POIModel createFromParcel(Parcel in) {
			return new POIModel(in);
		}

		@Override
		public POIModel[] newArray(int size) {
			return new POIModel[size];
		}
	};

	public void setPOIId(String poiId) {
		mPOIId = poiId;
	}

	public String getPOIId() {
		return mPOIId;
	}

	public void setPOIName(String poiName) {
		mPOIName = poiName;
	}

	public String getPOIName() {
		return mPOIName;
	}

	public void setPOIDistance(String poiDistance) {
		mPOIDistance = poiDistance;
	}

	public String getPOIDistance() {
		return mPOIDistance;
	}

	public void setPOISubject(String poiSubject) {
		mPOISubject = poiSubject;
	}

	public String getPOISubject() {
		return mPOISubject;
	}

	public void setPOIType1(String poiType1) {
		mPOIType1 = poiType1;
	}

	public String getPOIType1() {
		return mPOIType1;
	}

	public void setPOIAddress(String poiAddress) {
		mPOIAddress = poiAddress;
	}

	public String getPOIAddress() {
		return mPOIAddress;
	}

	public void setPOIInfo(String poiInfo) {
		mPOIInfo = poiInfo;
	}

	public String getPOIInfo() {
		return mPOIInfo;
	}

	public void setPOILat(Double poiLat) {
		mPOILat = poiLat;
	}

	public Double getPOILat() {
		return mPOILat;
	}

	public void setPOILong(Double poiLong) {
		mPOILong = poiLong;
	}

	public Double getPOILong() {
		return mPOILong;
	}

	public void setPOIKeywords(ArrayList<String> poiKeywords) {
		mPOIKeyWords = poiKeywords;
	}

	public ArrayList<String> getPOIKeywords() {
		return mPOIKeyWords;
	}

	public void setPOIPeriod(String poiPeriod) {
		mPOIPeriod = poiPeriod;
	}

	public String getPOIPeriod() {
		return mPOIPeriod;
	}


	public void setIdentifier(String identifier) { mIdentifier = identifier; }

	public  String getIdentifier() { return mIdentifier; }

	public void setContributor(String contributor) { mContributor = contributor; }

	public String getContributor() { return mContributor; }

	public void setOpen(String open) { mOpen = open; }

	public String getOpen() { return mOpen; }

	public void setMedia(ArrayList<String> media) { mMedia = media; }

	public ArrayList<String> getMedia() { return mMedia; }

	public void setPOISource(String poiSource) {
		mPOISource = poiSource;
	}

	public String getPOISource() {
		return mPOISource;
	}

	public void setUrl(ArrayList<String> url) {
		mUrl = url;
	}

	public ArrayList getUrl() {
		return mUrl;
	}

	public void setPOIPics(ArrayList<String> poiPics) {
		mPOIPics = poiPics;
	}

	public ArrayList<String> getPOIPics() {
		return mPOIPics;
	}

	public void setPOIAudios(ArrayList<String> poiAudios) {
		mPOIAudios = poiAudios;
	}

	public ArrayList<String> getPOIAudios() {
		return mPOIAudios;
	}

	public void setPOIMovies(ArrayList<String> poiMovies) {
		mPOIMovies = poiMovies;
	}

	public ArrayList<String> getPOIPMovies() {
		return mPOIMovies;
	}

	public void setPOIAudioTours(ArrayList<String> poiAudioTours) {
		mPOIAudioTours = poiAudioTours;
	}

	public ArrayList<String> getPOIAudioTours() {
		return mPOIAudioTours;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject.toString();
	}

	public JSONObject getJsonObject() {
		try {
			return new JSONObject(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mPOIId);
		dest.writeString(mPOIName);
		dest.writeString(mPOIDistance);
		dest.writeString(mPOISubject);
		dest.writeString(mPOIType1);
		dest.writeString(mPOIAddress);
		dest.writeString(mPOIInfo);
		dest.writeDouble(mPOILat);
		dest.writeDouble(mPOILong);
		dest.writeString(mPOIPeriod);
		dest.writeString(mPOISource);
		dest.writeString(mIdentifier);
		dest.writeString(mContributor);
		dest.writeString(mOpen);
		dest.writeStringList(mMedia);
		dest.writeStringList(mPOIKeyWords);
		dest.writeStringList(mUrl);
		dest.writeStringList(mPOIPics);
		dest.writeStringList(mPOIAudios);
		dest.writeStringList(mPOIMovies);
		dest.writeStringList(mPOIAudioTours);
		dest.writeString(jsonObject);
	}
}
