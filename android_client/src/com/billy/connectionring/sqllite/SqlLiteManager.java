package com.billy.connectionring.sqllite;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.DBModel_Component;
import com.billy.connectionring.model.Dash;

// http://here4you.tistory.com/50 �����ϱ�

public class SqlLiteManager {

	// singleton Object
	private static SqlLiteManager instance;

	// db���� ���
	private static final String tablename_dash = "dash";
	private static final String tablename_component = "component";
	public static final int dbVersion = 1;

	// db ���� ��ü
	private OpenHelper dbHelper;
	private SQLiteDatabase db;


	private Context _context;


	// singleton getInstance
	public static synchronized SqlLiteManager getInstance(Context context) {
		if(instance == null) {
			instance = new SqlLiteManager(context);

		}
		return instance;
	}


	public SqlLiteManager(Context context) {
		this._context = context;
		this.dbHelper = new OpenHelper(_context, null, dbVersion);
		db = dbHelper.getWritableDatabase();
	}

	/*
	public void insertComponentData(Component component)
	{
		DBModel dbModel = SqlLiteConverter.Component2DBModel(
				component);

		String sql = "insert into " + tablename_component + " values(" + 
				dbModel.getCid() + "," + dbModel.getDid() + ", '" +
				dbModel.getTypec() + "', '" + dbModel.getTitle() + "', '" + 
				dbModel.getContent() + "', '" + dbModel.getRegdate() + 
				"', '" + dbModel.getDate() + "', '" +  dbModel.isVisible() +	"');" ;

		try {
			db.execSQL(sql);
		}catch(Exception e) {
			return;
		}
	}
	 */

	public void insertData(String query)
	{
		synchronized (java.lang.Object.class) {
			try {
				db.execSQL(query);
			}catch(Exception e) {
				//Toast.makeText(_context, "������Ʈ ������ �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				return;
			}
		}
	}

	public void updateData(String query)
	{
		synchronized (java.lang.Object.class) {
			try {
				db.execSQL(query);
			}catch(Exception e) {
				//Toast.makeText(_context, "������Ʈ ���ſ� �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				return;
			}
		}
	}

	public void removeData(String query)
	{
		synchronized (java.lang.Object.class) {
			try {
				db.execSQL(query);
			}catch(Exception e) {
				//Toast.makeText(_context, "������Ʈ ������ �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				return;
			}
		}
	}

	public Component selectComponent(String query)
	{
		synchronized (java.lang.Object.class) {
			Cursor result = db.rawQuery(query, null);

			// result(Cursor ��ü)�� ��� ������ false ����
			if (result.moveToFirst()) {

				Component component = SqlLiteConverter.DBModel2Component(new DBModel_Component(result.getInt(0), result.getInt(1), 
						result.getString(2), result.getString(3), result.getString(4), result.getInt(5), result.getInt(6), result.getString(7), 
						result.getString(8), result.getString(9)));
				result.close();
				return component;
			}

			result.close();
			return null;
		}
	}

	public ArrayList<Component> selectComponentList(String query)
	{
		synchronized (java.lang.Object.class) {
			try {
			Cursor results = db.rawQuery(query, null);

			results.moveToFirst();
			ArrayList<Component> components = new ArrayList<Component>();

			while (!results.isAfterLast()) {

				Component component = SqlLiteConverter.DBModel2Component(new DBModel_Component(results.getInt(0), results.getInt(1), 
						results.getString(2), results.getString(3), results.getString(4), results.getInt(5), results.getInt(6), results.getString(7), 
						results.getString(8), results.getString(9)));

				components.add(component);
				results.moveToNext();
			}
			results.close();
			return components;
			}catch(Exception e) {
				//Toast.makeText(_context, "������Ʈ ���ſ� �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				return null;
			}
		}
	}

	@SuppressWarnings({ "deprecation", "null" })
	public ArrayList<Component> selectComponentMemoAll() {
		synchronized (java.lang.Object.class) {
			String sql = "select * from " + tablename_component + " where typec = 'memo';";
			Cursor results = db.rawQuery(sql, null);

			results.moveToFirst();
			ArrayList<Component> components = new ArrayList<Component>();

			while (!results.isAfterLast()) {

				Component component = SqlLiteConverter.DBModel2Component(new DBModel_Component(results.getInt(0), results.getInt(1), 
						results.getString(2), results.getString(3), results.getString(4), results.getInt(5), results.getInt(6), results.getString(7), 
						results.getString(8), results.getString(9)));

				components.add(component);
				results.moveToNext();
			}
			results.close();
			return components;
		}
	}

	@SuppressWarnings({ "deprecation", "null" })
	public ArrayList<Component> selectComponentCalendarAll() {
		synchronized (java.lang.Object.class) {
			String sql = "select * from " + tablename_component + " where typec = 'calendar';";
			Cursor results = db.rawQuery(sql, null);

			results.moveToFirst();
			ArrayList<Component> components = new ArrayList<Component>();

			while (!results.isAfterLast()) {

				Component component = SqlLiteConverter.DBModel2Component(new DBModel_Component(results.getInt(0), results.getInt(1), 
						results.getString(2), results.getString(3), results.getString(4), results.getInt(5), results.getInt(6), results.getString(7), 
						results.getString(8), results.getString(9)));

				components.add(component);
				results.moveToNext();
			}
			results.close();
			return components;
		}
	}



	public Dash selectDash(String query)
	{
		synchronized (java.lang.Object.class) {
			Cursor result = db.rawQuery(query, null);

			// result(Cursor ��ü)�� ��� ������ false ����
			if (result.moveToFirst()) {

				Dash dash = new Dash(result.getInt(0), result.getString(1));

				result.close();
				return dash;
			}
			result.close();
			return null;
		}
	}

	@SuppressWarnings({ "deprecation", "null" })
	public ArrayList<Dash> selectDashList(String query) {
		synchronized (java.lang.Object.class) {

			try {
			Cursor results = db.rawQuery(query, null);
			results.moveToFirst();
			ArrayList<Dash> dashes = new ArrayList<Dash>();
			while (!results.isAfterLast()) {

				Dash dash = new Dash(results.getInt(0), results.getString(1)); 

				dashes.add(dash);
				results.moveToNext();
			}

			results.close();
			return dashes;

			}catch(Exception e) {
				return null;
			}
		}
	}

	@SuppressWarnings({ "deprecation", "null" })
	public ArrayList<Dash> selectDashAll() {
		synchronized (java.lang.Object.class) {
			String sql = "select * from " + tablename_dash + ";";
			Cursor results = db.rawQuery(sql, null);

			results.moveToFirst();
			ArrayList<Dash> dashes = new ArrayList<Dash>();

			while (!results.isAfterLast()) {

				Dash dash = new Dash(results.getInt(0), results.getString(1)); 

				dashes.add(dash);
				results.moveToNext();
			}
			results.close();
			return dashes;
		}
	}

	// db helper
	public class OpenHelper extends SQLiteOpenHelper {

		private static final String dirPath = "/mnt/sdcard/Connectionring";
		private static final String dbName = "/connectionring.db";

		private void CreateFolder() {
			String str = Environment.getExternalStorageState();
			if ( str.equals(Environment.MEDIA_MOUNTED)) {

				File file = new File(dirPath); 
				if( !file.exists() )  // ���ϴ� ��ο� ������ �ִ��� Ȯ��
					file.mkdirs();
			}
		}
		public OpenHelper(Context context, CursorFactory factory,
				int version) {
			super(context, dirPath+dbName, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			// TODO Auto-generated method stub

			CreateFolder();
			String sql = "CREATE TABLE `dash` (" +
					"`did`	INTEGER," +  
					"`dashname`	VARCHAR(100)," +
					"PRIMARY KEY(did)" +
					");";
			database.execSQL(sql);

			sql = "CREATE TABLE `component` (" +
					"`cid`	INTEGER,"+
					"`did`	INTEGER,"+
					"`typec`	VARCHAR(45),"+
					"`sub`	VARCHAR(45),"+
					"`content`	VARCHAR(1000),"+
					"`x`	INTEGER,"+
					"`y`	INTEGER,"+
					"`regdate`	VARCHAR(100),"+
					"`date`	VARCHAR(100),"+
					"`visible`	VARCHAR(5),"+
					"PRIMARY KEY(cid)" +
					");";
			database.execSQL(sql);

			sql = "CREATE TABLE `bookmark` (" +
					"`id`	INTEGER," +
					"`type`	VARCHAR(45),"+
					"PRIMARY KEY(id)" +
					");";
			database.execSQL(sql);
			
			sql = "CREATE TABLE `quick` (" +
					"`id`	INTEGER," +
					"`type`	VARCHAR(45),"+
					"PRIMARY KEY(id)" +
					");";
			database.execSQL(sql);
		}


		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			onCreate(database);
		}

	}
}
