package com.billy.connectionring;

import java.util.Date;

import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;

public class SlidingChildModel {

	private int type;
	private int cid;
	private int did;
	private String dashname;
	private String typec;
	private String title;
	private String content;
	private Date regdate;
	private Date date;
	private boolean visible;
	
	public SlidingChildModel(Component component)
	{
		this.type = Constants.ITEM_MEMO;
		this.cid = component.getCid();
		this.did = component.getDid();
		this.typec = component.getTypec();
		this.title = component.getTitle();
		this.content = component.getContent();
		this.regdate = component.getRegdate();
		this.date = component.getDate();
		this.visible = component.isVisible();
	}
	
	public SlidingChildModel(Dash dash)
	{
		this.type = Constants.ITEM_MEMOGROUP;
		this.did = dash.getDid();
		this.dashname = dash.getDashname();
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
	
	public int getType() {
		return type;
	}
	
	public String getDashname() {
		return dashname;
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
		int hours = date.getHours();
		if(hours > 12 )
			res = false;
		else
			res = true;
		return res;
	}
}
