package com.wimoto.app.widgets.pickers;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.wimoto.app.R;

public abstract class BasePickerView extends RelativeLayout {

	protected Context mContext;
	protected boolean mIsShown;

	protected String mSensorCharacteristic;
	
	public BasePickerView(Context context, String sensorCharacteristic) {
		super(context);
		
		mContext = context;
		mSensorCharacteristic = sensorCharacteristic;
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
		        RelativeLayout.LayoutParams.WRAP_CONTENT,
		        RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		setLayoutParams(layoutParams);
	}
	
	protected void initButtons() {
		Button cancelButton = (Button) findViewById(R.id.alarmCancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}	
        });
        
        Button saveButton = (Button) findViewById(R.id.alarmSaveButton);
        saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();			
			}	
        });
	}
	
	public String getSensorCharacteristic() {
		return mSensorCharacteristic;
	}
	
	public void show() {
		if (mIsShown) {
			return;
		}
		
		 Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alarm_in);
		 startAnimation(animation);
		 
		 setVisibility(View.VISIBLE);
		 
		 mIsShown = true;
	}
	
	public void hide() {
		if (!mIsShown) {
			return;
		}
		
		 Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alarm_out);
		 animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
			 
		 });
		 
		 startAnimation(animation);
		 mIsShown = false;
	}
	
	abstract void cancel();
	abstract void save();
}
