package com.flashlightfree;

import android.app.Application;
import android.graphics.Typeface;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Dmytro Denysenko on 5/6/15.
 */
public class App extends Application {
    private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;

    @Override
    public void onCreate() {
        super.onCreate();
        initTypeface();

        initUM();
    }

    private void initUM(){
        //init umeng
        //打开调试模式（debug）后，在集成测试模式中，数据实时发送，不受发送策略控制
        MobclickAgent.setDebugMode(false);
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //您需要在程序的入口 Activity 中添加
        MobclickAgent.updateOnlineConfig(this);
        //如果enable为true，SDK会对日志进行加密
        AnalyticsConfig.enableEncrypt(false);
    }

    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);

    }
}
