package com.billy.connectionring.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.util.Log;

import com.billy.connectionring.Constants;

public class Component {
	private int cid;
	private int did;
	private String typec;
	private String title;
	private String content;
	private int x;
	private int y;
	private Date regdate;
	private Date date;
	private boolean visible;

	public Component(int cid, int did, String typec, String title, String content, Date regdate, Date date, boolean visible)
	{
		this.cid = cid;
		this.did = did;
		this.typec = typec;
		this.title = title;
		this.content = content;

		Random random = new Random();
		this.x = random.nextInt(Constants.PC_MAX_X);
		this.y = random.nextInt(Constants.PC_MAX_Y);
		this.regdate = regdate;
		this.date = date;
		this.visible = visible;
	}

	public Component(int cid, int did, String typec, String title, String content, int x, int y, Date regdate, Date date, boolean visible)
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


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		super.toString();
		String res =
				"cid : " + cid +"\n" +
						"did : " + did +"\n" +
						"typec : " + typec +"\n" +
						"title : " + title +"\n" +
						"content : " + content +"\n" +
						"regdate : " + regdate +"\n" +
						"date : " + date +"\n" +
						"visible : " + visible; 

		return res;
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

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}



	@SuppressWarnings("deprecation")
	public static boolean isAM(Date date) {

		boolean res = true;
		
		SimpleDateFormat transFormat = new SimpleDateFormat("kk");
		String to = transFormat.format(date);
		int hours = Integer.parseInt(to);
		
		if(hours > 12 )
			res = false;
		else
			res = true;
		
		return res;
	}
}
