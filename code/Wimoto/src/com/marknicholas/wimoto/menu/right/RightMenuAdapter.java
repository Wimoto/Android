package com.marknicholas.wimoto.menu.right;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.managers.SensorsManager;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.utils.AppContext;

public class RightMenuAdapter extends BaseAdapter {

private ArrayList<Sensor> mSensors;
	
	public RightMenuAdapter() {
		mSensors = new ArrayList<Sensor>();
	}
	
	public void updateSensors(ArrayList<Sensor> sensors) {
		mSensors.clear();
		mSensors.addAll(sensors);
		
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mSensors.size();
	}

	@Override
	public Sensor getItem(int position) {
		return mSensors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RightMenuView rightMenuView = null;
		if (convertView == null) {
			rightMenuView = new RightMenuView(AppContext.getContext());
		} else {
			rightMenuView = (RightMenuView)convertView;
		}
		
		rightMenuView.setSensor(mSensors.get(position));
		
		View deleteButton = rightMenuView.findViewById(R.id.delete_button);
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteSensor(mSensors.get(position));
			}
		});
		
		return rightMenuView;
	}

	private void deleteSensor(Sensor sensor) {
		SensorsManager.getInstance().unregisterSensor(sensor);
	}
}
