package com.billy.connectionring.connection;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.style.UpdateAppearance;
import android.util.Log;

import com.billy.connectionring.Constants;
import com.billy.connectionring.MainActivity;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.Dash;
import com.billy.connectionring.model.InviteDash;
import com.billy.connectionring.sqllite.SqlLiteConverter;
import com.billy.connectionring.sqllite.SqlLiteManager;
import com.billy.connectionring.sqllite.SqlLiteQuery;


public class JSONHelper extends Observable{
	
	private static JSONHelper instance;
	private static Timer mTimer;
	private static TimerTask mTask;
	private WebsocketHelper webSocketHelper;
	private SqlLiteManager dbManager;
	private Context context;
	private MainActivity main;
	
	
	public void setActivity(MainActivity main)
	{
		this.main = main;
	}
	
	public void update()
	{
		new Thread(new Runnable() {
		    @Override
		    public void run() {    
		        main.runOnUiThread(new Runnable(){
		            @Override
		             public void run() {
		            	main.updateSlidingList();
		             }
		        });
		    }
		}).start();
	}
	
	public static JSONHelper getInstance(Context context)
	{
		if(instance == null) {
			instance = new JSONHelper(context);
		}
		mTimer = new Timer();
		mTask = new TimerTask() {
            @Override
            public void run() {
            	instance.update();
            }
        };
		return instance;
	}
	
	private JSONHelper(Context context) {
		this.context = context;
	}
	
	private void updateHelper()
	{
		if(this.webSocketHelper == null)
			this.webSocketHelper = WebsocketHelper.getInstance(context);
		if(this.dbManager == null)
			this.dbManager = SqlLiteManager.getInstance(context);
	}
	public String ToJSON(Object object, int action_index) {
		updateHelper();
		User user = User.getInstance();
		String str_json = null;
		
		if(action_index == Constants.req_liveMessage) {
			str_json = "{\"type\":\"liveMessage\",\"id\":\"" + user.getId() + "\",\"target\":\"android\"}";
		}
		else if(action_index == Constants.req_login) {
			str_json = "{\"type\" : \"login\", \"id\" : \"" + user.getId() + "\", \"pw\" : \"" + user.getPw() + "\", \"target\": \"android\"}";
		}
		else if(action_index == Constants.req_logout) {
			str_json = "{\"type\" : \"logout\", \"id\" : \"" + user.getId() + "\", \"target\": \"android\"}";
		}
		else if(action_index == Constants.req_dashNew) {
			Dash dash = (Dash)object;
			str_json = "{\"type\":\"dashNew\",\"id\": \"" + user.getId() + "\", \"dashname\":\"" + dash.getDashname() + "\",\"imgsrc\":\"defalult_phone_dash.png\" , \"target\": \"android\"}";
		}
		else if(action_index == Constants.req_dashUpdate) {
			Dash dash = (Dash)object;
			str_json = "{\"type\":\"dashUpdate\",\"id\":\"" + user.getId() +"\",\"did\":\"" + dash.getDid() + "\",\"dashname\":\"" + dash.getDashname() + "\",\"imgsrc\":\"defalult_phone_dash.png\" , \"target\": \"android\",\"msg\":\"dashname\"}";
		}
		else if(action_index == Constants.req_dashDelete) {
			Dash dash = (Dash)object;
			str_json = "{\"type\":\"dashDelete\",\"did\":\"" + dash.getDid() +"\" , \"id\":\"" + user.getId() + "\", \"target\": \"android\"}";
		}
		else if(action_index == Constants.req_getDashList) {
			str_json = "{\"type\":\"getDashList\", \"id\":\"" + user.getId() + "\", \"target\": \"android\"}";
		}
		else if(action_index == Constants.req_getDash) {
			int did = (Integer)object;
			str_json = "{\"type\":\"getDash\" , \"id\":\"" + user.getId() + "\",\"did\":\"" + did + "\",\"target\":\"android\"}";
		}
		else if(action_index == Constants.req_componentNew) {
			Component component = (Component)object;
			String date = SqlLiteConverter.Date2String(component.getDate());
			
			if(component.getTypec().equals("calendar")) {
				date= date.substring(0, 10);
				date = date + " 00:00:00";
			}
			
			String text = component.getContent().toString();
			text = text.replace("\n", "\\n");
			
			str_json = "{\"type\":\"componentNew\",\"id\":\"" + user.getId() + "\",\"did\":\"" + component.getDid() + 
					"\",\"cuid\":\"1\" ,\"typec\":\"" + component.getTypec() + "\", \"sub\":\"" + component.getTitle() +
					"\", \"content\":\"" + text + "\", \"x\":\"" + component.getX() + "\",\"y\":\"" + component.getY() + 
					"\", \"date\" : \"" + date + "\", \"target\": \"android\"}";
		}
		else if(action_index == Constants.req_componentUpdate) {
			/*Component component = (Component)object;
			webSocketHelper.sendMessage(component, Constants.req_componentDelete);
			
			
			webSocketHelper.sendMessage(component, Constants.req_componentNew);*/
			
			/*String date = SqlLiteConverter.Date2String(component.getDate());
			String msg = (component.getTypec().equals(Constants.COMPONENT_ITEMTYPE_MEMO))?"content":"date";
			String text = component.getContent().toString();
			text = text.replace("\n", "\\n");		
			
			str_json = "{\"type\":\"componentUpdate\",\"id\":\"" + user.getId() + "\",\"did\":\"" + component.getDid() + 
					"\", \"cid\":\"" + component.getCid() + "\", \"typec\":\"" + component.getTypec() + "\", \"sub\":\"" + component.getTitle() +
					"\", \"content\":\"" + text + "\", \"x\":\"" + component.getX() + "\",\"y\":\"" + component.getY() + "\", \"date\" : \"" + date + 
					"\", \"target\": \"android\",\"msg\":\"" + msg + "\"}";*/
		}
		else if(action_index == Constants.req_componentDelete) {
			Component component = (Component)object;
			str_json = "{\"type\":\"componentDelete\",\"id\":\"" + user.getId() + "\",\"did\":\"" + component.getDid() +
					"\", \"cid\":\"" + component.getCid() + "\" ,\"target\": \"android\"}";
		}
		else if(action_index == Constants.req_getComponentList) {
			int did = (Integer)object;
			str_json = "{\"type\":\"getComponentList\",\"id\":\"" + user.getId() + "\", \"did\":\"" + did + "\",\"target\": \"android\"}";
		}
		else if(action_index == Constants.req_getDashComponentList) {
			str_json = "{\"type\":\"getDashComponentList\",\"id\":\"" + user.getId() + "\", \"target\":\"android\"}";
		}
		else if(action_index == Constants.req_inviteResponse) {
			InviteDash i_dash = (InviteDash)object;
			String isInviteResponse = (i_dash.isInviteResponse())?"TRUE":"FALSE";
			
			str_json = "{\"type\":\"inviteResponse\",\"did\":\"" + 10 + "\",\"id\":\"" + user.getId() + "\",\"fid\":\"" + i_dash.getFid() +
					"\",\"dashname\":\"" + i_dash.getDashname() + "\",\"Response\":\"" + isInviteResponse + "\", \"target\":\"android\"}";
		}
		else if(action_index == Constants.req_getInviteList) {
			str_json = "{\"type\":\"getInviteList\",\"id\":\"" + user.getId() + "\",\"target\":\"android\"}";
		}else if(action_index == Constants.registGCM) {
			String registID = (String)object;
			str_json =  "{\"type\":\"registGCM\",\"id\":\"" + user.getId() + "\",\"gcmId\":\"" + registID + "\",\"target\":\"android\"}";
		}
		
			

		return str_json;
	}

	public void FromJSON(String str_json) throws JSONException {
		updateHelper();
		JSONObject object = new JSONObject(str_json);
		
		if(!object.getString("target").equals("android"))
			return;
		String type = object.getString("type");
		
		if(type.equals("liveMessage")) { 
			webSocketHelper.sendMessage(null, Constants.req_liveMessage);
			Log.d("JSON", "JSON LOG : live Message ����");
		}
		
		else if(type.equals("login")) {
			if(object.getString("return").equals("True"))
			{
				setChanged();
		        notifyObservers("LoginOK");
		        webSocketHelper.setLogin(true);
				// login ���� ����
				Log.d("JSON", "JSON LOG : 로그인 Success");
			}else {
				// login ���� ����
				setChanged();
		        notifyObservers("LoginFail");
		        webSocketHelper.setLogin(false);
				Log.d("JSON", "JSON LOG : 로그인 Fail");
				webSocketHelper.closeWebSocket();
			} 
		}
		
		else if(type.equals("logout")) {
			if(object.getString("return").equals("True"))
			{
				// logout ���� ����
				webSocketHelper.setLogin(false);
				//webSocketHelper.closeWebSocket();
				Log.d("JSON", "JSON LOG : 로그아웃 Success");
			}else {
				// logout ���� ����
				Log.d("JSON", "JSON LOG : 로그아웃 Fail");
			} 
		}
		
		else if(type.equals("logoutAction")) { 
			// logout ���� ����
			//webSocketHelper.closeWebSocket();
			Log.d("JSON", "JSON LOG : 로그아웃 액션 Success");
		}
		
		// Response {"type":"dashNew","return":"False" , "target": "android"}
		// ���� �� �߸��� ���� �Է��̶�� toast ����ֱ�.
		else if(type.equals("dashNew")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : DashNew Fail");
			} 
		}
		
		else if(type.equals("dashNewAction")) { 
			int did = object.getInt("did");
			String dashname = object.getString("dashname");
			
			String query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(did, dashname));
			dbManager.insertData(query);
			Log.d("JSON", "JSON LOG : DashNew Success");
		}
		
		else if(type.equals("dashUpdate")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : DashUpdate Fail");
			} 
		}
		
		else if(type.equals("dashUpdateAction")) {
			int did = object.getInt("did");
			String dashname = object.getString("dashname");
			
			String query = SqlLiteQuery.get_UPDATE_DASH_QUERY(new Dash(did, dashname));
			dbManager.updateData(query);
			Log.d("JSON", "JSON LOG : DashUpdate Success");
		}
		
		
		else if(type.equals("dashDelete")) { 
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : dashDelete Fail");
			} 
		}
		
		else if(type.equals("dashDeleteAction")) { 
			int did = object.getInt("did");
			
			String query = SqlLiteQuery.get_DELETE_DASH_QUERY(dbManager, did);
			dbManager.removeData(query);
			Log.d("JSON", "JSON LOG : dashDeleteAction Success");
		}
		
		else if(type.equals("getDashList")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : getDashList Fail");
				return;
			} 
			JSONArray dashList = object.getJSONArray("dashList");	
			ArrayList<Dash> server_dashlist = new ArrayList<Dash>();
			
			for(int i = 0; i < dashList.length(); i++) {
				int did = dashList.getJSONObject(i).getInt("did");
				String dashname = dashList.getJSONObject(i).getString("dashname");
				
				server_dashlist.add(new Dash(did, dashname));
				
				String query = SqlLiteQuery.get_SELECT_ITEM_DASH(did);
				if(dbManager.selectDash(query) != null) {
					// db에 있다면
					query = SqlLiteQuery.get_UPDATE_DASH_QUERY(new Dash(did, dashname));
					dbManager.updateData(query);
				}else {
					// db에 없다면
					query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(did, dashname));
					dbManager.insertData(query);
				}
			}
			
			//synchronizeDash(server_dashlist);
			Log.d("JSON", "JSON LOG : getDashList Success");
		}
		
		else if(type.equals("getDash")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : getDash Fail");
				return;
			} 
			int did = object.getInt("did");
			String dashname = object.getString("dashname");
			String query = SqlLiteQuery.get_SELECT_ITEM_DASH(did);
			
			if(dbManager.selectDash(query) != null) {
				// db에 있다면
				query = SqlLiteQuery.get_UPDATE_DASH_QUERY(new Dash(did, dashname));
				dbManager.updateData(query);
			}else {
				// db에 없다면
				query = SqlLiteQuery.get_INSERT_DASH_QUERY(new Dash(did, dashname));
				dbManager.insertData(query);
			}
			Log.d("JSON", "JSON LOG : getDash Success");
		}
		
		else if(type.equals("getComponentList") || type.equals("getDashComponentList")) { 
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : getComponentList Fail");
				return;
			} 
			
			JSONArray componentList = object.getJSONArray("componentList");
			ArrayList<Component> server_cidlist = new ArrayList<Component>();
			for(int i = 0; i < componentList.length(); i++) {
				
				int cid = componentList.getJSONObject(i).getInt("cid");
				int did = componentList.getJSONObject(i).getInt("dash_did");
				String typec = componentList.getJSONObject(i).getString("typec");
				String sub = componentList.getJSONObject(i).getString("sub");
				String content = componentList.getJSONObject(i).getString("content");
				int x = componentList.getJSONObject(i).getInt("x");
				int y = componentList.getJSONObject(i).getInt("y");
				Date regdate = SqlLiteConverter.String2Date2(componentList.getJSONObject(i).getString("regdate"));
				Date date = SqlLiteConverter.String2Date2(componentList.getJSONObject(i).getString("date"));
				boolean visible = (componentList.getJSONObject(i).getString("visible").equals("y"))?true:false;
		    	
				server_cidlist.add(new Component(cid,  did, typec, sub, content, x, y, regdate, date, visible));
				
				String query = SqlLiteQuery.get_SELECT_ITEM_COMPONENT(cid);
				
				if(dbManager.selectComponent(query) != null) {
					// db에 있다면
					Log.d("JSON", "JSON LOG : update");
					query = SqlLiteQuery.get_UPDATE_COMPONENT_QUERY(new Component(cid,  did, typec, sub, content, x, y, regdate, date, visible));
					dbManager.updateData(query);
				}else {
					// db에 없다면
					query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(cid,  did, typec, sub, content, regdate, date, visible));
					dbManager.insertData(query);
				}
			}

			ArrayList<Component> components= dbManager.selectComponentList("select * from component");
			ArrayList<Dash> dashes = dbManager.selectDashAll();
			
			for(int i = 0; i < components.size(); i++)
			{
				boolean isDelete = true;
				for(int j = 0; j < dashes.size(); j++)
				{
					if(dashes.get(j).getDid() == components.get(i).getDid())
					{
						isDelete = false;
						break;
					}
				}
				if(isDelete == true)
				{
					String query = SqlLiteQuery.get_DELETE_COMPONENT_QUERY(components.get(i).getCid());
					dbManager.removeData(query);
				}
			}
			
			mTimer.schedule(mTask, 2000);
			//synchronizeComponent(server_cidlist);
			setChanged();
	        notifyObservers("SynchronizeComponent");
	        
			Log.d("JSON", "JSON LOG : getComponentList Success");
		}
		
		else if(type.equals("componentNew")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : ComponentNew Fail");
				Log.d("kkk", object.getString("msg").toString());
			} 
		}
		
		
		else if(type.equals("componentNewAction")) { 
			int cid = object.getInt("cid");
			int did = object.getInt("did");
			String typec = object.getString("typec");
			String title = object.getString("sub");
			String content = object.getString("content");
			Date regdate = new Date();
			Date date = SqlLiteConverter.String2Date(object.getString("date"));
			
			String query = SqlLiteQuery.get_INSERT_COMPONENT_QUERY(new Component(cid, did, typec, title, content, regdate, date, true));
			dbManager.insertData(query);

			mTimer.schedule(mTask, 2000);
			/*setChanged();
	        notifyObservers("UpdateComponent");*/
		}
		
		else if(type.equals("componentUpdate")) { 
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : componentUpdate Fail");
			} 
			
		}
		
		else if(type.equals("componentUpdateAction")) {
			int cid = object.getInt("cid");
			int did = object.getInt("did");
			String typec = object.getString("typec");
			String title = object.getString("sub");
			String content = object.getString("content");
			int x = object.getInt("x");
			int y = object.getInt("y");
			Date regdate = new Date();
			Date date = SqlLiteConverter.String2Date(object.getString("date"));
			
			String query = SqlLiteQuery.get_UPDATE_COMPONENT_QUERY(new Component(cid, did, typec, title, content, x, y, regdate, date, true));
			dbManager.updateData(query);

			mTimer.schedule(mTask, 2000);
			/*setChanged();
	        notifyObservers("UpdateComponent");*/

			Log.d("JSON", "JSON LOG : componentUpdateAction Success");
		}
		
		else if(type.equals("componentDelete")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : componentDelete Fail");
			} 
		}
		
		else if(type.equals("componentDeleteAction")) {
			int cid = object.getInt("cid");
			String query = SqlLiteQuery.get_DELETE_COMPONENT_QUERY(cid);
			dbManager.removeData(query);

	        Log.d("JSON", "JSON LOG : componentDeleteAction Success1");
	        mTimer.schedule(mTask, 2000);
			/*
			setChanged();
	        notifyObservers("UpdateComponent");*/
	        
	        Log.d("JSON", "JSON LOG : componentDeleteAction Success2");
		}
		
		else if(type.equals("dashInvite")) {
			Log.d("JSON", "JSON LOG : dashInvite Success");
		}
		
		else if(type.equals("inviteResponseTrue")) {
			Log.d("JSON", "JSON LOG : inviteResponseTrue Success");
		}
		
		else if(type.equals("inviteResponseFalse")) {
			Log.d("JSON", "JSON LOG : inviteResponseFalse Success");
		}
		
		else if(type.equals("getInviteList")) {
			Log.d("JSON", "JSON LOG : getInviteList Success");
		}else if(type.equals("registGCM")) {
			if(object.getString("return").equals("False"))
			{
				Log.d("JSON", "JSON LOG : GCM Fail");
			} 
			Log.d("JSON", "JSON LOG : GCM SUCCESS");
		}
	}
	
	
	public void synchronizeDash(ArrayList<Dash> server_dashlist)
	{
		ArrayList<Dash> add_list = new ArrayList<Dash>();
		ArrayList<Dash> client_dash = dbManager.selectDashAll();
		
		
		for(int i = 0; i < client_dash.size(); i++)
		{
			int client_did = client_dash.get(i).getDid();
			boolean isAddable = true;
			for(int j = 0; j < server_dashlist.size(); j++)
			{
				int server_did = server_dashlist.get(j).getDid();
				if(client_did == server_did)
				{
					isAddable = false;
					break;
				}
			}
			if(isAddable)
				add_list.add(client_dash.get(i));
		}
		
		for(int i = 0; i < add_list.size(); i++) {
			Log.d("addlist", add_list.toString());
			String query = SqlLiteQuery.get_DELETE_DASH_QUERY(add_list.get(i).getDid());
			dbManager.removeData(query);
			webSocketHelper.sendMessage(add_list.get(i), Constants.req_dashNew);
		}		
		
		
		setChanged();
        notifyObservers("SynchronizeDash");
	}
	
	public void synchronizeComponent(ArrayList<Component> server_cidlist)
	{

		ArrayList<Component> add_list = new ArrayList<Component>();
		ArrayList<Component> client_component = dbManager.selectComponentMemoAll();
		
		for(int i = 0; i < client_component.size(); i++)
		{
			int client_cid = client_component.get(i).getCid();
			Date client_date = client_component.get(i).getRegdate();
			boolean isAddable = true;
			for(int j = 0; j < server_cidlist.size(); j++)
			{
				int server_cid = server_cidlist.get(j).getCid();
				Date server_date = server_cidlist.get(j).getRegdate();
				if(client_cid == server_cid)
				{
					isAddable = false;
					break;
					// 클라가 최신이라면.
					/*if(client_date.compareTo(server_date) > 0)
						add_list.add(client_component.get(i));
					break;*/
				}
			}
			if(isAddable)
				add_list.add(client_component.get(i));
		}
		Log.d("add_list_size", Integer.toString(add_list.size()));
		for(int i = 0; i < add_list.size(); i++) {
			String query = SqlLiteQuery.get_DELETE_COMPONENT_QUERY(add_list.get(i).getCid());
			dbManager.removeData(query);
			webSocketHelper.sendMessage(add_list.get(i), Constants.req_componentNew);
		}
		setChanged();
        notifyObservers("SynchronizeComponent");
	}
	
	
	@Override
	public void addObserver(Observer observer) {
		// TODO Auto-generated method stub
		super.addObserver(observer);
		
	}
}