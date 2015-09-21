package com.wimoto.app.screens.searchsensor;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wimoto.app.AppContext;
import com.wimoto.app.bluetooth.DiscoveryListener;
import com.wimoto.app.bluetooth.WimotoDevice;

public class SearchSensorAdapter extends BaseAdapter implements DiscoveryListener {
	
	private AppContext mContext;
	private ArrayList<WimotoDevice> mDevices;
	
	public SearchSensorAdapter(AppContext context) {
		mContext = context;
		mDevices = new ArrayList<WimotoDevice>();		
	}
	
	public void refresh() {
		mDevices.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDevices.size();
	}

	@Override
	public WimotoDevice getItem(int position) {
		return mDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SensorView sensorView = null;
		if (convertView == null) {
			sensorView = new SensorView(mContext);
		} else {
			sensorView = (SensorView)convertView;
		}
		
		sensorView.setWimotoDevice(mDevices.get(position));
		
		return sensorView;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
		
		mContext.getSensorsManager().startScan(this);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
		
		mContext.getSensorsManager().stopScan();		
	}

	@Override
	public void onWimotoDeviceDiscovered(WimotoDevice wimotoDevice) {
		mDevices.add(wimotoDevice);
		
		mContext.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
}
