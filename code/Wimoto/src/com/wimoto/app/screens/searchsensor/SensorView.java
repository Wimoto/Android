package com.wimoto.app.screens.searchsensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.MainActivity;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;

public class SensorView extends LinearLayout implements PropertyChangeListener {
	
	private Context mContext;
	
	private WimotoDevice mDevice;
	private TextView mRssiTextView;
	
	public SensorView(Context context) {
		super(context);
		
		this.mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sensor_view, this);
        
        mRssiTextView = (TextView)findViewById(R.id.sensor_rssi);
	}

	public void setWimotoDevice(WimotoDevice device) {
		if (mDevice != null) {
			if (mDevice.equals(device)) {
				return;
			}
			
			mDevice.removeChangeListener(this);
		}
		
		mDevice = device;
		
		mDevice.addChangeListener(this, WimotoDevice.WIMOTO_DEVICE_FIELD_RSSI);
		
		TextView titleView = (TextView)findViewById(R.id.sensor_title);
		titleView.setText(mDevice.getName());
		
		TextView idView = (TextView)findViewById(R.id.sensor_id);
		idView.setText(mDevice.getId());		
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		mDevice.removeChangeListener(this);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (mContext == null) {
			return;
		}
		
		((MainActivity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String propertyName = event.getPropertyName();
				Object newValue = event.getNewValue();
				if (WimotoDevice.WIMOTO_DEVICE_FIELD_RSSI.equals(propertyName)) {
					mRssiTextView.setText((Integer)newValue + "dB");
				}
			}
		});
	}

//	@Override
//	public void update(Observable observable, final Object data) {
//		Log.e("", "RSSFFE " + data.getClass());
//		
//		if (data instanceof Integer) {
//			((AppContext) getContext()).runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					int rssiLevel = ((Integer) data).intValue();
//					
//					if (rssiLevel == 0) {
//						mRssiTextView.setVisibility(View.INVISIBLE);
//					} else {
//						mRssiTextView.setVisibility(View.VISIBLE);
//						mRssiTextView.setText(rssiLevel + "dB");
//					}				
//				}
//			});
//		}
//	}
}
