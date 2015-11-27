package com.flashlightfree.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flashlightfree.BaseActivity;
import com.oas.sc.myflash.R;

public class FunPlice extends BaseActivity {
	private int[] bgcolor={
			Color.rgb(255,15,0),
			Color.rgb(0,0,0),
			Color.rgb(255,15,0),
			Color.rgb(0,0,0),
			Color.rgb(255,15,0),
			Color.rgb(0,0,0),
			Color.rgb(0,0,255),
			Color.rgb(0,0,0),
			Color.rgb(0,0,255),
			Color.rgb(0,0,0),
			Color.rgb(0,0,255),
			Color.rgb(0,0,0)};
	private int[] bgflashtime={80,50,80,50,80,250,80,50,80,50,80,250};
	private TextView warmingtv;

	private Handler show_handler;
	private Runnable show_runnable;
	private boolean firsttime=true;
	private int warmingcounter=-1;
	private TextView titletv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policelight);
		warmingtv= (TextView) findViewById(R.id.warmingtv);
		titletv= (TextView) findViewById(R.id.titletv);
		warmingtv.setBackgroundColor(bgcolor[1]);
		setBrightness((float) 1.0);
		show_handler = new Handler();
		show_runnable = new Runnable() {
			@Override
			public void run() {
				warmingcounter++;
				if(warmingcounter==16){
					titletv.setVisibility(View.INVISIBLE);
				}

				warmingtv.setBackgroundColor(bgcolor[warmingcounter%12]);
				show_handler.postDelayed(this,bgflashtime[warmingcounter%12]);
			}
		};
	}

	@Override
	protected String getUmPageName() {
		return "FunPlice";
	}

	@Override
    public void onDestroy(){
        super.onDestroy();
        show_handler.removeCallbacks(show_runnable);
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
}
