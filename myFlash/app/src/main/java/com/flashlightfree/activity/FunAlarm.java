package com.flashlightfree.activity;

import android.app.Service;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flashlightfree.BaseActivity;
import com.oas.sc.myflash.R;

public class FunAlarm extends BaseActivity {
	private TextView warming1_tv;
	private TextView warming2_tv;
	
	private Handler show_handler;
	private Runnable show_runnable;
	private int warmingcounter=0;
	private boolean firsttime=true;
	private TextView titletv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warninglight);

		warming1_tv= (TextView) findViewById(R.id.warming1_tv);
		warming2_tv= (TextView) findViewById(R.id.warming2_tv);
		titletv= (TextView) findViewById(R.id.titletv);
		setBrightness((float) 1.0);
		

		show_handler = new Handler();
		show_runnable = new Runnable() {
			@Override
			public void run() {
				warmingcounter++;
				if(warmingcounter==4)
					titletv.setVisibility(View.INVISIBLE);
				if(warmingcounter%2==1)
				{
				 warming1_tv.setBackgroundResource(R.drawable.warning1);
				 warming2_tv.setBackgroundResource(R.drawable.warning2);
				}
				else
				{
				 warming1_tv.setBackgroundResource(R.drawable.warning2);
				 warming2_tv.setBackgroundResource(R.drawable.warning1);	
				}
				show_handler.postDelayed(this,750);
			}
		};
	}

	@Override
	protected String getUmPageName() {
		return "FunAlarm";
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	AudioManager audioManager=null;
    	audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);

    if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, 
                AudioManager.FLAG_SHOW_UI);
        	return true;
    }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                AudioManager.ADJUST_RAISE, 
                AudioManager.FLAG_SHOW_UI);    
    		return true;
    }else if(keyCode==KeyEvent.KEYCODE_BACK){
    	return super.onKeyDown(keyCode, event);
    }else{
        return super.onKeyDown(keyCode, event);
    }
    }
	
	public void setBrightness(float f){
		  WindowManager.LayoutParams lp = getWindow().getAttributes();
		  lp.screenBrightness = f;   
		  getWindow().setAttributes(lp);
		 } 
	
	@Override
    protected void onResume(){
        super.onResume();
        if(firsttime){
        	firsttime=false;
        	show_handler.postDelayed(show_runnable,750);
        }
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        show_handler.removeCallbacks(show_runnable);
    }

}
