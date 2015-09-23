package com.wimoto.app.screens.searchsensor;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;

public class SensorView extends LinearLayout implements Observer {
	
	private WimotoDevice mDevice;
	private TextView mRssiTextView;
	
	public SensorView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sensor_view, this);
        
        mRssiTextView = (TextView)findViewById(R.id.sensor_rssi);
	}

	public void setWimotoDevice(WimotoDevice device) {
		if (mDevice != null) {
			if (mDevice.equals(device)) {
				return;
			}
			
			//mDevice.deleteObserver(this);
		}
		
		mDevice = device;
		//mConnection.addObserver(this);
		
		TextView titleView = (TextView)findViewById(R.id.sensor_title);
		titleView.setText(mDevice.getName());
		
		TextView idView = (TextView)findViewById(R.id.sensor_id);
		idView.setText(mDevice.getId());		
	}

	@Override
	public void update(Observable observable, final Object data) {
		Log.e("", "RSSFFE " + data.getClass());
		
		if (data instanceof Integer) {
			((AppContext) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					int rssiLevel = ((Integer) data).intValue();
					
					if (rssiLevel == 0) {
						mRssiTextView.setVisibility(View.INVISIBLE);
					} else {
						mRssiTextView.setVisibility(View.VISIBLE);
						mRssiTextView.setText(rssiLevel + "dB");
					}				
				}
			});
		}
	}
}
