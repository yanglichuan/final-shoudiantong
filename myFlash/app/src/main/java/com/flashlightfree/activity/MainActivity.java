package com.flashlightfree.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flashlightfree.BaseActivity;
import com.flashlightfree.FlashVariables;
import com.flashlightfree.utils.PowerLED;
import com.flashlightfree.view.circleprogress.CircleProgressView;
import com.oas.sc.myflash.R;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.List;


public class MainActivity extends BaseActivity {
    private MediaPlayer mMediaPlayer;
    private Button powerbtn;
    private TextView titletv;
    private PowerLED powerled;

    private CircleProgressView circleView;

    private int[] batterystatuspercent = {75, 30, 0};
    private int[] title_color = {Color.rgb(51, 51, 51), Color.rgb(48, 210, 255)};
    //是否是自启动
    private boolean autostarled = true;

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    private GuillotineAnimation ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        circleView = (CircleProgressView) findViewById(R.id.circleView);
        circleView.setValue(0);

        //***************************
        if(bStatus){
            View statusSp = findViewById(R.id.status_sp);
            ViewGroup.LayoutParams params = statusSp.getLayoutParams();
            params.height = getStatusHeight(this);
            statusSp.setLayoutParams(params);
            statusSp.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.status_sp).setVisibility(View.GONE);
        }
        //***************************

        autostarled = mSp.getBoolean(FlashVariables.Auto_Start_LED, true);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        root = (FrameLayout) findViewById(R.id.root);
        contentHamburger = findViewById(R.id.content_hamburger);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);


        //***************************
        if(bStatus){
            View statusSp = guillotineMenu.findViewById(R.id.status_sp2);
            ViewGroup.LayoutParams params = statusSp.getLayoutParams();
            params.height = getStatusHeight(this);
            statusSp.setLayoutParams(params);
            statusSp.setVisibility(View.VISIBLE);
        } else {
            guillotineMenu.findViewById(R.id.status_sp2).setVisibility(View.GONE);
        }
        //***************************


        ac = new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
                guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(100)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        initListner();


        powerbtn = (Button) findViewById(R.id.powerbtn);
        titletv = (TextView) findViewById(R.id.titletv);
        //用来播放声音
        mMediaPlayer = new MediaPlayer();
        //Typeface fonttype = Typeface.createFromAsset(this.getAssets(),"fonts/justusitalic.ttf");
        Typeface fonttype = Typeface.createFromAsset(this.getAssets(), "fonts/digifaw.ttf");
        titletv.setTypeface(fonttype);
        //获得是否自动启动应用
        if (autostarled) {
            playSounds(R.raw.turnon);
        }
        powerled = new PowerLED();
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        titletv.setTextColor(title_color[0]);
        powerbtn.setVisibility(View.GONE);
        if (!powerled.getIsOn() && autostarled) {
            powerled.turnOn();
            powerbtn.setBackgroundResource(R.drawable.button_on);
            titletv.setTextColor(title_color[1]);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //添加一些动画
                    animRotate(powerbtn, false);
                }
            }, 200);
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //添加一些动画
                    animFadeScaleIn(powerbtn, false, null);
                }
            },200);
        }
        powerbtn.setOnClickListener(this);
    }


    private Handler mHandler = new Handler();


    private void animRotate(final View view, boolean bReverse){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",-180,0);
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(0);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        if(bReverse){
            objectAnimator.reverse();
        }else{
            objectAnimator.start();
        }
    }

    private void animFadeScaleIn(final View view,boolean bReverse, Animator.AnimatorListener listener){
        ObjectAnimator objectAnimator = null;
        ObjectAnimator objectAnimator2 = null;
        ObjectAnimator objectAnimator3 = null;
        if(bReverse){
            objectAnimator = ObjectAnimator.ofFloat(view,"alpha",1,0);
            objectAnimator2 = ObjectAnimator.ofFloat(view,"scaleX",1,0);
            objectAnimator3 = ObjectAnimator.ofFloat(view,"scaleY",1,0);
        }else{
            objectAnimator = ObjectAnimator.ofFloat(view,"alpha",0,1);
            objectAnimator2 = ObjectAnimator.ofFloat(view,"scaleX",0,1);
            objectAnimator3 = ObjectAnimator.ofFloat(view,"scaleY",0,1);
        }
        objectAnimator.setDuration(300);
        objectAnimator.setRepeatCount(0);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator2.setDuration(300);
        objectAnimator2.setRepeatCount(0);
        objectAnimator2.setInterpolator(new DecelerateInterpolator());
        objectAnimator3.setDuration(300);
        objectAnimator3.setRepeatCount(0);
        objectAnimator3.setInterpolator(new DecelerateInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        set.playTogether(objectAnimator,objectAnimator2,objectAnimator3);
        set.start();
    }




    @Override
    protected String getUmPageName() {
        return "MainActivity";
    }


    private void initListner() {
        checkBox = (CheckBox) findViewById(R.id.auto_setup);
        checkBox.setChecked(autostarled);

        findViewById(R.id.screen_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, FunScreenColor.class);
                startActivity(it);
            }
        });
        findViewById(R.id.alarm_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, FunAlarm.class);
                startActivity(it);
            }
        });
        findViewById(R.id.plice_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, FunPlice.class);
                startActivity(it);
            }
        });
        findViewById(R.id.sos_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (powerled.getIsOn()) {
                    powerled.turnOff();
                    powerbtn.setBackgroundResource(R.drawable.button_off);
                    titletv.setTextColor(title_color[0]);
                    //关闭
                    powerbtn.setBackgroundResource(R.drawable.button_off);
                }
                powerled.Destroy();
                Intent it = new Intent(MainActivity.this, FunSos.class);
                startActivity(it);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autostarled = isChecked;
                if (autostarled) {
                    mSp.edit().putBoolean(FlashVariables.Auto_Start_LED, true).commit();
                } else {
                    mSp.edit().putBoolean(FlashVariables.Auto_Start_LED, false).commit();
                }
            }
        });
    }


    CheckBox checkBox = null;
    Toolbar toolbar = null;
    FrameLayout root = null;
    View contentHamburger = null;


    @Override
    public void onDestroy() {
        super.onDestroy();
        powerled.Destroy();
        unregisterReceiver(mBatInfoReceiver);
    }

    @Override
    public void onBackPressed() {
        if(ac.isOpened()){
            ac.close();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.powerbtn:
                playSounds(R.raw.click);
                if (!powerled.getIsOn()) {
                    powerled.turnOn();
                    powerbtn.setBackgroundResource(R.drawable.button_on);
                    titletv.setTextColor(title_color[1]);
                } else {
                    powerled.turnOff();
                    powerbtn.setBackgroundResource(R.drawable.button_off);
                    titletv.setTextColor(title_color[0]);
                }
                break;
            default:
                break;
        }
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int intLevel = intent.getIntExtra("level", 0);
                int intScale = intent.getIntExtra("scale", 100);
                onBatteryInfoReceiver(intLevel, intScale);
            }
        }
    };


    public void onBatteryInfoReceiver(int intLevel, int intScale) {
        int bp = intLevel * 100 / intScale;
        circleView.setValueAnimated(bp);
//        if (bp >= batterystatuspercent[2]) {
//            batterytv.setBackgroundResource(batterystatusimgs[2]);
//        }
//        if (bp >= batterystatuspercent[1]) {
//            batterytv.setBackgroundResource(batterystatusimgs[1]);
//        }
//        if (bp >= batterystatuspercent[0]) {
//            batterytv.setBackgroundResource(batterystatusimgs[0]);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audioManager = null;
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_SHOW_UI);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_SHOW_UI);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            powerled.turnOff();
            powerbtn.setBackgroundResource(R.drawable.button_off);
            titletv.setTextColor(title_color[0]);
            return super.onKeyDown(keyCode, event);
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private boolean isRunApp(String packageName) {
        PackageInfo pi;
        try {
            pi = getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            // resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);   
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(
                    resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                return true;
            } else {
                return false;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 4) {
                powerled = new PowerLED();
            }
        }
    }


    private void playSounds(int sid) {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = MediaPlayer.create(MainActivity.this, sid);

		/* ׼������ */
        // mMediaPlayer.prepare();
        /* ��ʼ���� */
        mMediaPlayer.start();
    }
}
