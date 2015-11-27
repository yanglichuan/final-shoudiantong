package com.flashlightfree.activity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flashlightfree.BaseActivity;
import com.oas.sc.myflash.R;

public class FunSos extends BaseActivity {
	Camera m_Camera;
	Camera.Parameters mParameters;
	private int[] bgcolor={
			Color.rgb(255,0,0),
			Color.rgb(0,0,0),
			Color.rgb(255,0,0),
			Color.rgb(0,0,0),
			Color.rgb(255,0,0),
			
			Color.rgb(0,0,0),
			
			Color.rgb(255,0,0),
			Color.rgb(0,0,0),			
			Color.rgb(255,0,0),
			Color.rgb(0,0,0),
			Color.rgb(255,0,0),
			
			Color.rgb(0,0,0),
			
			Color.rgb(255,0,0),
			Color.rgb(0,0,0),
			Color.rgb(255,0,0),
			Color.rgb(0,0,0),
			Color.rgb(255,0,0),
			
			Color.rgb(0,0,0)
			
			};
	
	private int[] bgflashtime = new int[] {
			300,
			300,
			300,	// ...	S
			300,
			300,
			//
			900,
			//
			900,
			300,
			900,	// ---	O
			300,
			900,
			//
			900,
			//
			300,
			300,
			300,	// ...	S
			300,
			300,
			//
			2100
	};
	
	private TextView warmingtv;

	private Handler show_handler;
	private Runnable show_runnable;
	private boolean firsttime=true;
	private int warmingcounter=-1;
	private TextView titletv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soslight);
		warmingtv= (TextView) findViewById(R.id.warmingtv);
		titletv= (TextView) findViewById(R.id.titletv);
		
		m_Camera = Camera.open();
		mParameters = m_Camera.getParameters();
		warmingtv.setBackgroundColor(bgcolor[1]);
		setBrightness((float) 1.0);
		show_handler = new Handler();
		show_runnable = new Runnable() {
			@Override
			public void run() {
				warmingcounter++;
				warmingtv.setBackgroundColor(bgcolor[warmingcounter%18]);
				if(warmingcounter==5){
					titletv.setVisibility(View.INVISIBLE);
				}

				if(warmingcounter%2==0)
				{
					mParameters = m_Camera.getParameters();
					mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					m_Camera.setParameters(mParameters);
				}else
				{
					mParameters = m_Camera.getParameters();
					mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					m_Camera.setParameters(mParameters);
				}
				show_handler.postDelayed(this,bgflashtime[warmingcounter%18]);
			}
		};
	}

	@Override
	protected String getUmPageName() {
		return "FunSos";
	}


	@Override
    protected void onResume() {
        super.onResume();
        if(firsttime){
        	firsttime=false;
        	show_handler.postDelayed(show_runnable,50);
        }
	}
	
	public void setBrightness(float f){
		  WindowManager.LayoutParams lp = getWindow().getAttributes();
		  lp.screenBrightness = f;   
		  getWindow().setAttributes(lp);
		 } 
	

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	AudioManager audioManager=null;
    	audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);

    if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
    	//Toast.makeText(main.this, "Down", Toast.LENGTH_SHORT).show();
    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                AudioManager.ADJUST_LOWER, 
                AudioManager.FLAG_SHOW_UI);
        	return true;
    }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP)
    {
    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                AudioManager.ADJUST_RAISE, 
                AudioManager.FLAG_SHOW_UI);    
    		return true;
    }else if(keyCode==KeyEvent.KEYCODE_BACK)
    {
    	show_handler.removeCallbacks(show_runnable);
    	m_Camera.release();
		m_Camera = null;
    	Intent intent = new Intent(); 
    	setResult(4, intent);
    	return super.onKeyDown(keyCode, event);    
    }else{
    return super.onKeyDown(keyCode, event);    
    }
    }
}
