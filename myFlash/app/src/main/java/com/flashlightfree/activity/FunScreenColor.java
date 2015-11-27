package com.flashlightfree.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.flashlightfree.BaseActivity;
import com.flashlightfree.utils.RecordFileUtils;
import com.flashlightfree.view.RevealLayout;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.oas.sc.myflash.R;

public class FunScreenColor extends BaseActivity {
    private RevealLayout mRevealLayout;
    private boolean mIsAnimationSlowDown = false;
    private boolean mIsBaseOnTouchLocation = false;
    private TextView tv1;
    private TextView tv2;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_screencolor);

        mRevealLayout = (RevealLayout) findViewById(R.id.reveal_layout);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        toColor = RecordFileUtils.getInstance(mContext).getColorData("color");
        tv1.setBackgroundColor(toColor);

        setupColorDialog();
    }

    @Override
    protected String getUmPageName() {
        return "FunScreenColor";
    }

    private int toColor = Color.GREEN;
    private  void setupColorDialog(){
        findViewById(R.id.reveal_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(mContext)
                        .setTitle(getString(R.string.screencolor))
                        .initialColor(toColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                toColor = selectedColor;
                                toColor(toColor);
                                RecordFileUtils.getInstance(mContext).setColorData("color",toColor);

                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });
    }


    boolean boratation = true;
    private void toColor(int toColor) {
        if (boratation) {
            boratation = false;
            tv2.setBackgroundColor(toColor);
        } else {
            boratation = true;
            tv1.setBackgroundColor(toColor);
        }
        if (mIsAnimationSlowDown) {
            mRevealLayout.next(2000);
        } else {
            mRevealLayout.next();
        }
    }
}
