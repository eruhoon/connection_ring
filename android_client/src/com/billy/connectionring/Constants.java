package com.billy.connectionring;

public class Constants {

	/**
	 * Slide Constants
	 */
	// DB Constants
	public final static String COMPONENT_ITEMTYPE_MEMO = "memo";
	public final static String COMPONENT_ITEMTYPE_CALENDAR = "calendar";
	
	// Slide Group Item
	public final static int SLIDE_ID = 0;
	public final static int SLIDE_ALLNOTE = 1;
	public final static int SLIDE_DASHBOARD = 2;
	public final static int SLIDE_BOOKMARK = 3;
	public final static int SLIDE_SETTINGS = 4;
	
	// Slide Child Item
	public final static int ITEM_MEMO = 0;
	public final static int ITEM_MEMOGROUP = 1;
	
	
	// Server Request Message Index
	public final static int req_liveMessage = 0;
	public final static int req_login = 1;
	public final static int req_logout = 2;
	public final static int req_dashNew = 3;
	public final static int req_dashUpdate = 4;
	public final static int req_dashDelete  = 5;
	public final static int req_getDashList = 6;
	public final static int req_getDash = 7;
	public final static int req_componentNew = 8;
	public final static int req_componentUpdate = 9;
	public final static int req_componentDelete = 10;
	public final static int req_getComponentList = 11;
	public final static int req_getDashComponentList = 12;
	public final static int req_inviteResponse = 13;
	public final static int req_getInviteList = 14;
	public final static int registGCM = 15;
	
	public final static int PC_MAX_X = 3000;
	public final static int PC_MAX_Y = 1600;
	
	
	public final static String TUTORIAL_TEXT = "** tutorial **\n"
			+ "너와나의 연결고리에 오신 것을 환영합니다.\n"
			+ "1. 하단에 (+) 버튼을 이용하여 메모 및 일정을 작성 할 수 있습니다.\n"
			+ "2. 우측에 슬라이드를 통해 Dashboard 및 Bookmark를 이용 할 수 있습니다.\n"
			+ "3. 앱을 실행하지 않고 우측 화면을 터치하여 Quick 서비스를 이용 할 수 있습니다.\n"
			+ "\n"
			+ "그럼 너와나의 연결고리에 다양한 서비스를 이용해보세요";
}
