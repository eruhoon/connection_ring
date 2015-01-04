package com.billy.connectionring.model;

public class InviteDash extends Dash{

	String fid;
	boolean inviteResponse;
	
	public InviteDash(int did, String dashname, String fid, boolean inviteResponse) {
		// TODO Auto-generated constructor stub
		super(did, dashname);
		this.fid = fid;
		this.inviteResponse = inviteResponse;
	}
	

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public boolean isInviteResponse() {
		return inviteResponse;
	}

	public void setInviteResponse(boolean inviteResponse) {
		this.inviteResponse = inviteResponse;
	}

}
