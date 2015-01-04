package com.billy.connectionring.sqllite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.DBModel_Component;

public class SqlLiteConverter {

	public static DBModel_Component Component2DBModel(Component component) {
		int cid = component.getCid();
		int did = component.getDid();
		String typec = component.getTypec();
		String title = component.getTitle();
		String content = component.getContent();
		int x = component.getX();
		int y = component.getY();
		String str_regdate = Date2String(component.getRegdate());
		String str_date = Date2String(component.getDate());
		String visible = (component.isVisible())?"y":"n";
		
		DBModel_Component dbModel = new DBModel_Component(cid, did, typec, title, content, x, y, str_regdate, str_date, visible);
		return dbModel;
	}
	
	@SuppressWarnings("deprecation")
	public static Component DBModel2Component(DBModel_Component dbModel) {
		
		int cid = dbModel.getCid();
    	int did = dbModel.getDid();
    	String typec = dbModel.getTypec();
    	String title = dbModel.getTitle();
    	String content = dbModel.getContent();
    	int x = dbModel.getX();
    	int y = dbModel.getY();
    	//  data :  yy//mm//dd//hh//mi//ss
    	
    	Date regdate = String2Date(dbModel.getRegdate());
    	Date date = String2Date(dbModel.getDate());
		
    	String str_visible = dbModel.isVisible();
    	
    	
    	boolean visible = false;
    	if(str_visible.equals("y")) visible = true;
    	else if(str_visible.equals("n")) visible = false;
		
		Component component = new Component(cid, did, typec, title, content, x, y, regdate, date, visible);
		return component;
	}
	

	
	public static String Date2String(Date date)
	{
		if(date == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String str_date = format.format(date);
		return str_date;
	}
	
	public static Date String2Date(String str_date) {
		
		if(str_date == null)
			return null;
		String subdatePart_1 = str_date.substring(0, 10);
		String subdatePart_2 = str_date.substring(11);
		
		String[] dayPart_1 = subdatePart_1.split("-");
		String[] dayPart_2 = subdatePart_2.split(":");
		
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(dayPart_1[0]), Integer.parseInt(dayPart_1[1])-1, Integer.parseInt(dayPart_1[2]), 
				Integer.parseInt(dayPart_2[0]), Integer.parseInt(dayPart_2[1]), Integer.parseInt(dayPart_2[2]));
		cal.set(Calendar.MILLISECOND, 0);
		Date date = cal.getTime();
		
		return date;
	}
	
	public static Date String2Date2(String str_date) {
		
		if(str_date == null)
			return null;
		TimeZone utc = TimeZone.getTimeZone("UTC");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		f.setTimeZone(utc);
		GregorianCalendar cal = new GregorianCalendar(utc);
		Date date = null;
		try {
			cal.setTime(f.parse(str_date));
			date = cal.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			date = new Date();
			return date;
		}
		return date;
	}
}
