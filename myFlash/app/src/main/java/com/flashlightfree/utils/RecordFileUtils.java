package com.flashlightfree.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import com.flashlightfree.FlashVariables;


public class RecordFileUtils {
	private static RecordFileUtils sp_util;
	private RecordFileUtils() {
	}

	private Context context;
	public static RecordFileUtils getInstance(Context ct) {
		if (sp_util == null) {
			synchronized (RecordFileUtils.class) {
				if (sp_util == null) {
					sp_util = new RecordFileUtils(ct);
					return sp_util;
				}
			}
		}
		return sp_util;
	}

	private RecordFileUtils(Context ct) {
		this.context = ct;
	}

	public String getStringData(String key) {
		SharedPreferences sp = getSP();
		return sp.getString(key, "");
	}


	public int getColorData(String key) {
		SharedPreferences sp = getSP();
		return sp.getInt(key, Color.GREEN);
	}

	public boolean setColorData(String key, int color){
		SharedPreferences sp = getSP();
		if (sp == null) {
			return false;
		}
		Editor editor = sp.edit();
		if (editor == null) {
			return false;
		}
		editor.putInt(key,color);
		editor.commit();
		return true;
	}


	public boolean getBooleanData(String key) {
		SharedPreferences sp = getSP();
		return sp.getBoolean(key, false);
	}
	public boolean getBooleanData(String key,boolean bInit) {
		SharedPreferences sp = getSP();
		return sp.getBoolean(key, bInit);
	}

	public boolean setStringData(String key, String value) {
		SharedPreferences sp = getSP();
		if (sp == null) {
			return false;
		}
		Editor editor = sp.edit();
		if (editor == null) {
			return false;
		}
		editor.putString(key, value);
		editor.commit();
		return true;
	}

	public boolean setBooleanData(String key, boolean value) {
		SharedPreferences sp = getSP();
		if (sp == null) {
			return false;
		}
		Editor editor = sp.edit();
		if (editor == null) {
			return false;
		}
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}

	private SharedPreferences getSP() {
		SharedPreferences sp = context.getSharedPreferences(
				FlashVariables.Setting_Info, Context.MODE_PRIVATE);
		return sp;
	}
}
