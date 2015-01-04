package com.billy.connectionring.sqllite;

import java.util.Date;

import com.billy.connectionring.Constants;
import com.billy.connectionring.model.Component;
import com.billy.connectionring.model.DBModel_Component;
import com.billy.connectionring.model.Dash;

public class SqlLiteQuery {

	private static final String tablename_dash = "dash";
	private static final String tablename_component = "component";
	private static final String tablename_bookmark = "bookmark";
	private static final String tablename_quick = "quick";




	//**////////////////////////////////////////////////////**//
	//**				Component Query						**//
	//**////////////////////////////////////////////////////**//

	/**
	 * INSERT
	 */

	public static String get_INSERT_COMPONENT_QUERY(Component component)
	{
		DBModel_Component dbModel = SqlLiteConverter.Component2DBModel(
				component);

		Date date = component.getDate();
		String str_day = SqlLiteConverter.Date2String(date);
		
		if(component.getTypec().equals(Constants.COMPONENT_ITEMTYPE_CALENDAR))
			str_day = str_day.substring(0, 10) + " 00:00:00";
		
		//String str_date = 
		String sql = "insert into " + tablename_component + " values(" + 
				dbModel.getCid() + "," + dbModel.getDid() + ", '" +
				dbModel.getTypec() + "', '" + dbModel.getTitle() + "', '" + 
				dbModel.getContent() + "', " + dbModel.getX() + ", " + dbModel.getY() + ", '" +
				dbModel.getRegdate() + "', '" + str_day + "', '" +  dbModel.isVisible() +	"');" ;

		
		return sql;
	}

	/**
	 * UPDATE
	 */
	public static String get_UPDATE_COMPONENT_QUERY(Component component)
	{
		DBModel_Component dbModel = SqlLiteConverter.Component2DBModel(
				component);

		Date date = component.getDate();
		String str_day = SqlLiteConverter.Date2String(date);
		
		if(component.getTypec().equals(Constants.COMPONENT_ITEMTYPE_CALENDAR))
			str_day = str_day.substring(0, 10) + " 00:00:00";
				
		//update tableName set a=? , b=? where
		String sql = "update " + tablename_component + " set cid = " + dbModel.getCid() + 
				", did = " + dbModel.getDid() + ", typec = '" + dbModel.getTypec() + 
				"', sub = '" + dbModel.getTitle() + "', content = '" + dbModel.getContent() +
				"', x = " + dbModel.getX() + ", y = " + dbModel.getY() + 
				" , regdate = '" + dbModel.getRegdate() + "', date = '" + str_day +
				"', visible = '" + dbModel.isVisible() + "' where cid = " + dbModel.getCid();
		
		return sql;
	}
	
	/**
	 * REMOVE
	 */
	public static String get_DELETE_COMPONENT_QUERY(int cid)
	{
		String sql = "delete from " + tablename_component + " where cid = " + cid + ";";

		return sql;
	}
	
	public static String get_LIST_DELETE_COMPONENT_QUERY(int did)
	{
		String sql = "delete from " + tablename_component + " where did = " + did + ";";

		return sql;
	}

	/**
	 * Select query naming regulation
	 * 1 item : get_SELECT_ITEM_***
	 * List : get_SELECT_LIST_****
	 * ALL : get_SELECT_ALL_
	 */

	public static String get_SELECT_ITEM_COMPONENT(int cid)
	{
		String query = "select * from component where cid = " + cid + ";";

		return query;
	}
	
	public static String get_SELECT_LIST_COMPONENT_MEMO_ORDERBYDAY()
	{
		String query = "select * from component where typec = 'memo' order  by regdate desc";

		return query;
	}
	
	public static String get_SELECT_LIST_DASHOFCOMPONENT_MEMO_ORDERBYDAY(int did)
	{
		String query = "select * from component where did = " + did + " AND typec = 'memo' order by regdate desc";

		return query;
	}

	public static String get_SELECT_LIST_COMPONENT_UPDATEDAY_ORDERBYDAY()
	{
		String query = "select distinct regdate from component where typec = 'memo' order by regdate desc";

		return query;
	}

	public static String get_SELECT_LIST_COMPONENT_DAY_ORDERBYDAY()
	{
		String query = "select * from component where typec = 'calendar'";
		
		return query;
	}
	
	public static String get_SELECT_LIST_COMPONENT_CALENDAR_ORDERBYDAY(String str_day)
	{
		//String str_day = SqlLiteConverter.Date2String(date);
		String query = "select * from component where date = '" + str_day + "'";
		
		return query;
	}
	//**////////////////////////////////////////////////////**//
	//**					Dash Query						**//
	//**////////////////////////////////////////////////////**//

	/**
	 * INSERT
	 */

	public static String get_INSERT_DASH_QUERY(Dash dash)
	{

		String sql = "insert into " + tablename_dash + " values(" + 
				dash.getDid() + ", '" + dash.getDashname() + "');" ;

		return sql;
	}

	/**
	 * UPDATE
	 */
	public static String get_UPDATE_DASH_QUERY(Dash dash)
	{
		
		String sql = "update " + tablename_dash + " set dashname = '" + dash.getDashname() + "' where did = " + dash.getDid() + ";";

		return sql;
	}
	
	/**
	 * REMOVE
	 */
	public static String get_DELETE_DASH_QUERY(SqlLiteManager dbManager, int did)
	{
		String query = SqlLiteQuery.get_LIST_DELETE_COMPONENT_QUERY(did);
		dbManager.removeData(query);
		
		String sql = "delete from " + tablename_dash + " where did = " + did + ";";

		return sql;
	}

	public static String get_DELETE_DASH_QUERY(int did)
	{
		String sql = "delete from " + tablename_dash + " where did = " + did + ";";

		return sql;
	}
	
	/**
	 * SELECT
	 * Select query regulation
	 * 1 item : get_SELECT_ITEM_***
	 * List : get_SELECT_LIST_****
	 * ALL : get_SELECT_ALL_
	 */
	

	public static String get_SELECT_ITEM_DASH(int did)
	{
		String query = "select * from dash where did = " + did + ";";

		return query;
	}
	
	public static String get_SELECT_LIST_DASH()
	
	{
		String query = "select * from dash;";

		return query;
	}





	//**////////////////////////////////////////////////////**//
	//**				Bookmark Query						**//
	//**////////////////////////////////////////////////////**//

	/**
	 * INSERT
	 */

	public static String get_INSERT_Bookmark_QUERY(int id, int type)
	{

		//String str_date = 
		String sql = "insert into " + tablename_bookmark + " values(" + 
				Integer.toString(id) + ", " + type + ");" ;

		return sql;
	}

	
	
	/**
	 * UPDATE
	 */

	/**
	 * REMOVE
	 */


	/**
	 * Select query naming regulation
	 * 1 item : get_SELECT_ITEM_***
	 * List : get_SELECT_LIST_****
	 * ALL : get_SELECT_ALL_
	 */
	
	public static String get_SELECT_LIST_COMPONENT_BOOKMARK()
	{
		String query = "select * from component where typec = 'memo' AND cid in(select id from bookmark where type = 0);";

		return query;
	}
	
	public static String get_SELECT_LIST_DASH_BOOKMARK()
	{
		String query = "select * from dash where did in(select id from bookmark where type = 1);";

		return query;
	}
	
	
	//**////////////////////////////////////////////////////**//
		//**				Quick Query						**//
		//**////////////////////////////////////////////////////**//

		/**
		 * INSERT
		 */

		public static String get_INSERT_Quick_QUERY(int id, int type)
		{

			//String str_date = 
			String sql = "insert into " + tablename_quick + " values(" + 
					Integer.toString(id) + ", " + type + ");" ;

			return sql;
		}

		
		
		/**
		 * UPDATE
		 */

		/**
		 * REMOVE
		 */

		public static String get_DELETE_COMPONENT_QUICK_QUERY(int cid)
		{
			String sql = "delete from " + tablename_quick + " where id = " + cid + " and type = "+ Constants.ITEM_MEMO+";";

			return sql;
		}
		
		public static String get_DELETE_DASH_QUICK_QUERY(int did)
		{
			String sql = "delete from " + tablename_quick + " where id = " + did + " and type = " + Constants.ITEM_MEMOGROUP + ";";

			return sql;
		}

		/**
		 * Select query naming regulation
		 * 1 item : get_SELECT_ITEM_***
		 * List : get_SELECT_LIST_****
		 * ALL : get_SELECT_ALL_
		 */
		
		public static String get_SELECT_LIST_COMPONENT_QUICK()
		{
			String query = "select * from component where  typec = 'memo' and cid in(select id from quick where type = 0);";

			return query;
		}
		
		public static String get_SELECT_LIST_DASH_QUICK()
		{
			String query = "select * from dash where did in(select id from quick where type = 1);";

			return query;
		}
		
		public static String get_SELECT_LIST_COMPONENT_EXCEPT_QUICK()
		{
			String query = "select * from component where typec = 'memo' and cid not in(select id from quick where type = 0);";

			return query;
		}
		
		public static String get_SELECT_LIST_DASH_EXCEPT_QUICK()
		{
			String query = "select * from dash where did not in(select id from quick where type = 1);";

			return query;
		}
}
