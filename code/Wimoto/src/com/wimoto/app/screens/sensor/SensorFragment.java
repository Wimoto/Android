package com.wimoto.app.screens.sensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mobitexoft.leftmenu.PageFragment;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.model.SensorValue;
import com.wimoto.app.model.SensorValueListener;
import com.wimoto.app.model.datalog.DataLog;
import com.wimoto.app.model.demosensors.ClimateDemoSensor;
import com.wimoto.app.model.demosensors.ThermoDemoSensor;
import com.wimoto.app.model.sensors.ClimateSensor;
import com.wimoto.app.model.sensors.GrowSensor;
import com.wimoto.app.model.sensors.Sensor;
import com.wimoto.app.model.sensors.Sensor.DataLoggerState;
import com.wimoto.app.model.sensors.Sensor.DataReadingListener;
import com.wimoto.app.model.sensors.SentrySensor;
import com.wimoto.app.model.sensors.ThermoSensor;
import com.wimoto.app.model.sensors.WaterSensor;
import com.wimoto.app.screens.sensor.climate.ClimateDemoSensorFragment;
import com.wimoto.app.screens.sensor.climate.ClimateSensorFragment;
import com.wimoto.app.screens.sensor.grow.GrowSensorFragment;
import com.wimoto.app.screens.sensor.sentry.SentrySensorFragment;
import com.wimoto.app.screens.sensor.thermo.ThermoDemoSensorFragment;
import com.wimoto.app.screens.sensor.thermo.ThermoSensorFragment;
import com.wimoto.app.screens.sensor.views.SensorFooterView;
import com.wimoto.app.screens.sensor.views.SensorFooterView.SensorFooterListener;
import com.wimoto.app.screens.sensor.water.WaterSensorFragment;
import com.wimoto.app.widgets.LastUpdateTextView;

public abstract class SensorFragment extends PageFragment implements PropertyChangeListener, SensorFooterListener, DataReadingListener {
	
	private static final String TAG_SENSOR = "sensor_tag";
	private static final String TAG_NO_SENSOR = "no_sensor_tag";
	
	protected ViewGroup mView;
	
	protected ImageView mBatteryImageView;
	protected TextView mRssiTextView;
	protected TextView mSensorNameText;
	protected LastUpdateTextView mLastUpdateText;
	
	private SensorFooterView mSensorFooterView;
	
	protected Sensor mSensor;
	
	public static SensorFragment createSensorFragment(Sensor sensor) {
		SensorFragment fragment = null;
		
		if (sensor instanceof ClimateDemoSensor) {
			fragment = new ClimateDemoSensorFragment();
		} else if (sensor instanceof ThermoDemoSensor) {
			fragment = new ThermoDemoSensorFragment();
		} else if (sensor instanceof ClimateSensor) {
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
			fragment.mSensor = sensor;
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
		
		setSensor(mSensor);
		
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
		
		mLastUpdateText = (LastUpdateTextView)mView.findViewById(R.id.last_updated_text);
		mLastUpdateText.setSensor(mSensor);
		
		mSensorNameText.setText(mSensor.getTitle());
		mSensorNameText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_STATE, true);
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_DL_STATE);
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_RSSI);
			mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_BATTERY_LEVEL);

			mSensor.setDataReadingListener(this);
		}
	}
	
	public String getFragmentId() {
		if (mSensor == null) {
			return TAG_NO_SENSOR;
		}
		return TAG_SENSOR + mSensor.getId();
	}

	private void updateBatteryLevel(int batteryLevel) {
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

	public SensorFooterView getSensorFooterView() {
		if (mSensorFooterView == null) {
			mSensorFooterView = (SensorFooterView) mView.findViewById(R.id.sensorFooterView);
			mSensorFooterView.setListener(this);
		}
		return mSensorFooterView;
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
				Object newValue = event.getNewValue();
				if (Sensor.SENSOR_FIELD_STATE.equals(propertyName)) {
					int state = getConnectionState(newValue);
					if (state == BluetoothProfile.STATE_CONNECTED) {
						mView.setBackgroundColor(getResources().getColor(getBackgroundColorRes()));
						mBatteryImageView.setVisibility(View.VISIBLE);
						mRssiTextView.setVisibility(View.VISIBLE);
						mSensorFooterView.showConnectionSensitiveButtons(true);
					} else {
						mView.setBackgroundColor(getResources().getColor(R.color.color_light_gray));
						mBatteryImageView.setVisibility(View.INVISIBLE);
						mRssiTextView.setVisibility(View.INVISIBLE);
						mSensorFooterView.showConnectionSensitiveButtons(false);
					}
				} else if (Sensor.SENSOR_FIELD_BATTERY_LEVEL.equals(propertyName)) {
					updateBatteryLevel((Integer)newValue);
				} else if (Sensor.SENSOR_FIELD_RSSI.equals(propertyName)) {
					mRssiTextView.setText((Integer)newValue + "dB");
				} else if (Sensor.SENSOR_FIELD_DL_STATE.equals(propertyName)) {
					DataLoggerState dataLoggerState = (DataLoggerState)newValue;
					mSensorFooterView.setDataLoggerState(dataLoggerState);
				}
			}
		});
	}
	
	protected int getConnectionState(Object object) {
		int state = 0;
		if (object != null) {
			if (object instanceof Integer) {
				state = ((Integer) object).intValue();
			} else if (object instanceof WimotoDevice.State) {
				state = ((WimotoDevice.State) object).ordinal();
			}
		}
		return state;
	}
	
	protected void showAlarmMessage(final Sensor sensor, final String propertyString, String message) {
		if (message.isEmpty()) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(sensor.getTitle() + " " + propertyString + " " + message)
    			.setCancelable(false)
    			.setPositiveButton("Switch off alarm",
    					new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								switchOffAlarm(propertyString);
								dialog.cancel();
							}
    					})
    			.setNegativeButton("Cancel",
    					new DialogInterface.OnClickListener() {
    						public void onClick(DialogInterface dialog, int id) {
    							dialog.cancel();
    						}
    					});
    	builder.create().show();
	}
	
	protected void switchOffAlarm(String propertyString) {
		//should be overridden in inheritors
	}
	
	@Override
	public void onLeftMenuClicked() {
		((SlidingFragmentActivity) getActivity()).showMenu();
	}

	@Override
	public void onRightMenuClicked() {
		((SlidingFragmentActivity) getActivity()).showSecondaryMenu();		
	}
	
	@Override
	public void onCloudSyncButtonClicked() {
		mSensor.requestSensorValues(new SensorValueListener() {
			@Override
			public void onSensorValuesReturned(ArrayList<SensorValue> sensorValues) {
				Log.e("onSensorValuesReturned", sensorValues.size() + "");
				didUpdateSensorReadingData(sensorValues);
			}
		});
	}
	
	@Override
	public void onEnableSensorDataLogger() {
		mSensor.enableDataLogger(true);
	}
	
	@Override
	public void onReadSensorDataLogger() {
		mSensor.readDataLogger();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
		}
	}

	@Override
	public void onPause() {
		if(mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		super.onPause();
	}
	
	protected boolean outOfRange(float currentValue, float maxValue, float minValue) {
		if(currentValue > maxValue || currentValue < minValue) {
			return true;
		}
		return false;
	}
	
	private AlertDialog mDialog;
	private long mLastShowTimeStamp;
	protected void showAlert(String message) {
		long currentShowTimeStamp = System.currentTimeMillis();
		if(mLastShowTimeStamp + 30 * 1000 > currentShowTimeStamp) {
			return;
		}
		mLastShowTimeStamp = currentShowTimeStamp;
		if(mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.dismiss();
				mDialog = null;
			}
		});
		builder.setCancelable(false);
		mDialog = builder.create();
		mDialog.show();
	}

	@Override
	public void didReadSensorDataLogger(ArrayList<DataLog> data) {
		Log.e("DataReadingListener", "didReadSensorDataLogger");
		
		File root = new File(Environment.getExternalStorageDirectory(), "Wimoto");
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, "AppData.json");

        FileWriter fileWriter;
        
		try {
			fileWriter = new FileWriter(file, true);
			
			for(DataLog dataLog : data) {
				fileWriter.write(dataLog.getJSONObject().toString() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendEmailWithFile(file);
	}

	@Override
	public void didUpdateSensorReadingData(ArrayList<SensorValue> data) {
		Log.e("DataReadingListener", "didUpdateSensorReadingData");

		File root = new File(Environment.getExternalStorageDirectory(), "Wimoto");
        if (!root.exists()) {
            root.mkdirs();
        }
        File file = new File(root, "AppData.json");

        FileWriter fileWriter;
        
		try {
			fileWriter = new FileWriter(file, true);
			
			for(SensorValue sensorValue : data) {
				fileWriter.write(sensorValue.getJSONObject().toString() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sendEmailWithFile(file);
	}
	
	private void sendEmailWithFile(File file) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Wimoto");
		intent.putExtra(Intent.EXTRA_TEXT, String.format("Content from Wimoto %s Sensor %s", mSensor.getCodename(), mSensor.getId()));
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		startActivity(Intent.createChooser(intent, "Send Email"));
	}
}
