package com.mmlab.m1.mini.model;

import io.realm.RealmObject;

/**
 * Created by waynewei on 2016/3/22.
 */
public class DEHUser extends RealmObject {

	private String id;
	private String pw;
	public void setId(String id){
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setPw(String pw) {this.pw = pw;}
	public String getPw() {return pw;}
}
