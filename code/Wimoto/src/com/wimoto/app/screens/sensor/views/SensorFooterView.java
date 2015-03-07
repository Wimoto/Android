package com.wimoto.app.screens.sensor.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wimoto.app.R;

public class SensorFooterView extends RelativeLayout {

	public interface SensorFooterListener {
		void onLeftMenuClicked();
		void onRightMenuClicked();
	}
	
	private Context mContext;
	private SensorFooterListener mListener;
	
	private ImageView mCloudSyncButton;
	private ImageView mDataLoggerButton;
	private ImageView mFirmwareButton;
	
	public SensorFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sensor_screen_footer, this);
        
        ImageView leftMenuControl = (ImageView) findViewById(R.id.leftMenuButton);
        leftMenuControl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onLeftMenuClicked();
				}
			}
		});
        
    	mCloudSyncButton = (ImageView) findViewById(R.id.cloudSyncButton);
    	mDataLoggerButton = (ImageView) findViewById(R.id.dataloggerButton);
    	mFirmwareButton = (ImageView) findViewById(R.id.firmwareButton);
        
        ImageView rightMenuControl = (ImageView) findViewById(R.id.rightMenuButton);
        rightMenuControl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightMenuClicked();
				}				
			}
		});
	}
	
	public void setListener(SensorFooterListener listener) {
		this.mListener = listener;
	}
	
	public void setLogo(int resourceId) {
		ImageView logoImageView = (ImageView)findViewById(R.id.logoImageView);
		logoImageView.setImageResource(resourceId);
	}
	
	public void showConnectionSensitiveButtons(boolean doShow) {
    	mCloudSyncButton.setVisibility((doShow)?View.VISIBLE:View.INVISIBLE);
    	mDataLoggerButton.setVisibility((doShow)?View.VISIBLE:View.INVISIBLE);
    	mFirmwareButton.setVisibility((doShow)?View.VISIBLE:View.INVISIBLE);
	}
}
