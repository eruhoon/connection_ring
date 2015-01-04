package com.billy.connectionring.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferenceSystem {

	final public static String prefFileName = "connectionring";

	@SuppressWarnings("static-access")
	public static void savePreferences(Context context, String key, String value) throws Exception {

		SharedPreferences pref = context.getSharedPreferences(prefFileName, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		try {
			editor.putString(key, value);
		} catch (Exception e) {
			throw new Exception("SjyPreferenceUtil_savePreferences fail");
		}
		editor.commit();
	}


	@SuppressWarnings("static-access")
	public static String getPreferences(Context context, String key) {
		SharedPreferences pref = context.getSharedPreferences(prefFileName, context.MODE_PRIVATE);
		String value = null;
		try{
			value = pref.getString(key, "");
			if(value!=null){
				value = value;
			}
		}catch (Exception e) {
		}

		return value;

	}

	@SuppressWarnings("static-access")
	public static void removePreferences(Context context, String key) {
		SharedPreferences pref = context.getSharedPreferences(prefFileName, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}

	@SuppressWarnings("static-access")
	public static void removeAllPreferences(Context context) {
		SharedPreferences pref = context.getSharedPreferences(prefFileName, context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
}
