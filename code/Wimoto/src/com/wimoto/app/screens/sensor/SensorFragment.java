package com.wimoto.app.screens.sensor;

import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.model.GrowSensor;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.model.SentrySensor;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.model.WaterSensor;
import com.wimoto.app.screens.sensor.climate.ClimateSensorFragment;
import com.wimoto.app.screens.sensor.grow.GrowSensorFragment;
import com.wimoto.app.screens.sensor.sentry.SentrySensorFragment;
import com.wimoto.app.screens.sensor.thermo.ThermoSensorFragment;
import com.wimoto.app.screens.sensor.water.WaterSensorFragment;
import com.mobitexoft.leftmenu.PageFragment;

public class SensorFragment extends PageFragment implements Observer {
	
	private static final String TAG_SENSOR = "sensor_tag";
	private static final String TAG_NO_SENSOR = "no_sensor_tag";
	
	protected View mView;
	
	protected ImageView mBatteryImageView;
	protected TextView mRssiTextView;
	protected TextView mSensorNameText;
	protected TextView mLastUpdateText;
	
	protected Sensor mSensor;
	
	public static SensorFragment createSensorFragment(Sensor sensor) {
		SensorFragment fragment = null;
		
		if (sensor instanceof ClimateSensor) {
			fragment = new ClimateSensorFragment();
		} else if (sensor instanceof GrowSensor) {
			fragment = new GrowSensorFragment();
		} else if (sensor instanceof SentrySensor) {
			fragment = new SentrySensorFragment();
		} else if (sensor instanceof ThermoSensor) {
			fragment = new ThermoSensorFragment();
		} else if (sensor instanceof WaterSensor) {
			fragment = new WaterSensorFragment();
		}
		
		if (fragment != null) {
			fragment.setSensor(sensor);
		}
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHeaderVisibility(View.GONE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initViews();	
		return mView;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (mSensor != null) {
			mSensor.deleteObserver(this);
		}
	}

	protected void initViews() {
		mBatteryImageView = (ImageView)mView.findViewById(R.id.battery_level);
		mRssiTextView = (TextView)mView.findViewById(R.id.rrsi_text);
		mSensorNameText = (TextView)mView.findViewById(R.id.sensor_name_text);
		mLastUpdateText = (TextView)mView.findViewById(R.id.last_updated_text);
		
		mSensorNameText.setText(mSensor.getTitle());
		
		if (mSensor.isConnected()) {
			setRssi(mSensor.getRssi());
		} else {
			mView.setBackgroundColor(getResources().getColor(R.color.color_light_gray));
		}
	}
	
	public void setSensor(Sensor sensor) {
		if (mSensor != null) {
			mSensor.deleteObserver(this);
		}
		
		mSensor = sensor;
		if (mSensor != null) {
			mSensor.addObserver(this);
		}
	}
	
	public String getFragmentId() {
		if (mSensor == null) {
			return TAG_NO_SENSOR;
		}
		return TAG_SENSOR + mSensor.getId();
	}

	private void setRssi(int rssi) {
		if (mRssiTextView == null) {
			return;
		}
		
		if (rssi == 0) {
			mRssiTextView.setVisibility(View.INVISIBLE);
		} else {
			mRssiTextView.setVisibility(View.VISIBLE);
			mRssiTextView.setText(rssi + "dB");
		}            	
	}
	
	@Override
	public void update(Observable observable, Object data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
				int rssiLevel = 0;
				if (mSensor != null) {
					rssiLevel = mSensor.getRssi();
				}
				
				setRssi(rssiLevel);
            }
        });
	}
}
