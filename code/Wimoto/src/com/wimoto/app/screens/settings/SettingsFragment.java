package com.wimoto.app.screens.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.mobitexoft.leftmenu.PageFragment;
import com.wimoto.app.R;

public class SettingsFragment extends PageFragment {

	private Switch mTemperatureUnitSwitch;
	
	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.settings_fragment, null);
	
		setTitleText(R.string.settings_title);
		
		mTemperatureUnitSwitch = (Switch)view.findViewById(R.id.temperatureUnitSwitch);
		setupTemperatureSwitch();
		
		return view;
	}
	
	private void setupTemperatureSwitch() {
		final SharedPreferences preferences = getActivity().getSharedPreferences("Wimoto", Context.MODE_PRIVATE);
		String temparatureUnitString = preferences.getString("cOrF", "C");
		mTemperatureUnitSwitch.setChecked(temparatureUnitString.equals("C")?false:true);
		
		mTemperatureUnitSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("cOrF", isChecked?"F":"C");
				editor.commit();
			}	
		});
	}
}
