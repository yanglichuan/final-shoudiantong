package com.flashlightfree;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

public abstract   class BaseActivity extends AppCompatActivity implements View.OnClickListener {
	protected SharedPreferences mSp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSp = getSharedPreferences(FlashVariables.Setting_Info,Context.MODE_PRIVATE);
		bStatus = dealStatusBar(this);
	}

	protected  boolean bStatus = false;

	protected abstract String getUmPageName();


	public static boolean dealStatusBar(Activity activity) {
		if (activity == null) {
			return false;
		}
		if (!(activity instanceof Activity)) {
			return false;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			return true;
		}
		return false;
	}
	/***
	 * 统计时长
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getUmPageName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getUmPageName());
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View view) {

	}
}
