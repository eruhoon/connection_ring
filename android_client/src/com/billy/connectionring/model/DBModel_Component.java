package com.billy.connectionring.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DBModel_Component {

	private int cid;
	private int did;
	private String typec;
	private String title;
	private String content;
	private int x;
	private int y;
	private String regdate;
	private String date;
	private String visible;
	
	public DBModel_Component(int cid, int did, String typec, String title, String content, int x, int y, String regdate, String date, String visible)
	{
		this.cid = cid;
		this.did = did;
		this.typec = typec;
		this.title = title;
		this.content = content;
		this.x = x;
		this.y = y;
		this.regdate = regdate;
		this.date = date;
		this.visible = visible;
	}
	

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}
	
	public String getTypec() {
		return typec;
	}

	public void setTypec(String typec) {
		this.typec = typec;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
	public String getRegdate() {
		
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String isVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

}