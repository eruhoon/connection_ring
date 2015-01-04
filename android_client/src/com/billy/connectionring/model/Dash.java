package com.billy.connectionring.model;

public class Dash {
	private int did;
	private String dashname;
	
	public Dash(int did, String dashname) {
		this.did = did;
		this.dashname = dashname;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		super.toString();
		String res =
						"did : " + did +"\n" +
						"dashname : " + dashname +"\n";
		return res;
	}
	
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public String getDashname() {
		return dashname;
	}
	public void setDashname(String dashname) {
		this.dashname = dashname;
	}
	
	
}
