package com.wimoto.app.screens.sensor.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wimoto.app.R;
import com.wimoto.app.model.sensors.Sensor.DataLoggerState;

public class SensorFooterView extends RelativeLayout {

	public interface SensorFooterListener {
		void onLeftMenuClicked();
		void onRightMenuClicked();
		void onCloudSyncButtonClicked();
		void onEnableSensorDataLogger();
		void onReadSensorDataLogger();
	}
	
	private Context mContext;
	private SensorFooterListener mListener;
	
	private ImageView mCloudSyncButton;
	
	private ImageView mEnableDataLoggerButton;
	private ImageView mReadDataLoggerButton;
	
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
    	mCloudSyncButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onCloudSyncButtonClicked();
				}
			}
    	});
    	
    	mEnableDataLoggerButton = (ImageView) findViewById(R.id.dataloggerEnableButton);
    	mEnableDataLoggerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onEnableSensorDataLogger();
				}
			}
    	});
    	
    	mReadDataLoggerButton = (ImageView) findViewById(R.id.dataloggerReadButton);
    	mReadDataLoggerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onReadSensorDataLogger();
				}
			}
    	});
    	
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
    	mFirmwareButton.setVisibility((doShow)?View.VISIBLE:View.INVISIBLE);
	}
	
	public void setDataLoggerState(DataLoggerState dataLoggerState) {
		switch(dataLoggerState) {
			case NONE: {
				Log.e("DataLoggerState", "None");
				mEnableDataLoggerButton.setVisibility(View.INVISIBLE);
				mReadDataLoggerButton.setVisibility(View.INVISIBLE);
				break;
			}
			case UNKNOWN: {
				Log.e("DataLoggerState", "Unknown");
				mEnableDataLoggerButton.setVisibility(View.VISIBLE);
				mEnableDataLoggerButton.setEnabled(false);
				mReadDataLoggerButton.setVisibility(View.INVISIBLE);
				break;
			}
			case DISABLED: {
				Log.e("DataLoggerState", "Disabled");
				mEnableDataLoggerButton.setVisibility(View.VISIBLE);
				mEnableDataLoggerButton.setEnabled(true);
				mReadDataLoggerButton.setVisibility(View.INVISIBLE);
				break;
			}
			case ENABLED: {
				Log.e("DataLoggerState", "Enabled");
				mEnableDataLoggerButton.setVisibility(View.INVISIBLE);
				mEnableDataLoggerButton.setEnabled(false);
				mReadDataLoggerButton.setVisibility(View.VISIBLE);
				mReadDataLoggerButton.setEnabled(true);
				break;
			}
			case READ: {
				Log.e("DataLoggerState", "Read");
				mEnableDataLoggerButton.setVisibility(View.INVISIBLE);
				mEnableDataLoggerButton.setEnabled(false);
				mReadDataLoggerButton.setVisibility(View.VISIBLE);
				mReadDataLoggerButton.setEnabled(false);
				break;
			}
			default: {
				break;
			}
		}
	}
}
