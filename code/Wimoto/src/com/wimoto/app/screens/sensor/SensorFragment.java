package com.wimoto.app.screens.sensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.mobitexoft.leftmenu.PageFragment;
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

public abstract class SensorFragment extends PageFragment implements PropertyChangeListener {
	
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
			mSensor.removeChangeListener(this);
		}
	}

	protected void initViews() {
		mView.setBackgroundColor(getResources().getColor(R.color.color_light_gray));
		
		mBatteryImageView = (ImageView)mView.findViewById(R.id.battery_level);
		mRssiTextView = (TextView)mView.findViewById(R.id.rrsi_text);
		mSensorNameText = (TextView)mView.findViewById(R.id.sensor_name_text);
		mLastUpdateText = (TextView)mView.findViewById(R.id.last_updated_text);
		
		mSensorNameText.setText(mSensor.getTitle());
		mSensorNameText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				mSensor.setTitle(mSensorNameText.getText().toString());
				return false;
			}
		});		
	}
	
	protected abstract int getBackgroundColorRes();
	
	public void setSensor(Sensor sensor) {
		if (mSensor != null) {
			mSensor.removeChangeListener(this);
		}
		
		mSensor = sensor;
		if (mSensor != null) {
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_CONNECTION);
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_BATTERY_LEVEL);
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_RSSI);
		}
	}
	
	public String getFragmentId() {
		if (mSensor == null) {
			return TAG_NO_SENSOR;
		}
		return TAG_SENSOR + mSensor.getId();
	}
	
	private void updateBateryLevel(int batteryLevel) {
		int resId = R.drawable.battery_low;
        if (batteryLevel > 75) {
        	resId = R.drawable.battery_full;
        } else if (batteryLevel > 50) {
        	resId = R.drawable.battery_high;
        } else if (batteryLevel > 25) {
        	resId = R.drawable.battery_medium;
        }
		mBatteryImageView.setImageDrawable(getResources().getDrawable(resId));
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (getActivity() == null) {
			return;
		}
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String propertyName = event.getPropertyName();
				if (Sensor.SENSOR_FIELD_CONNECTION.equals(propertyName)) {
					if (event.getNewValue() == null) {
						mView.setBackgroundColor(getResources().getColor(R.color.color_light_gray));
						mBatteryImageView.setVisibility(View.INVISIBLE);
						mRssiTextView.setVisibility(View.INVISIBLE);
					} else {
						mView.setBackgroundColor(getResources().getColor(getBackgroundColorRes()));
						mBatteryImageView.setVisibility(View.VISIBLE);
						mRssiTextView.setVisibility(View.VISIBLE);
					}
				} else if (Sensor.SENSOR_FIELD_BATTERY_LEVEL.equals(propertyName)) {
					updateBateryLevel((Integer)event.getNewValue());
				} else if (Sensor.SENSOR_FIELD_RSSI.equals(propertyName)) {
					mRssiTextView.setText((Integer)event.getNewValue() + "dB");
				}	
			}
		});
	}
	
}
