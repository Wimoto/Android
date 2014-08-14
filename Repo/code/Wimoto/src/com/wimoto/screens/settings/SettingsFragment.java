package com.wimoto.screens.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marknicholas.wimoto.R;
import com.mobitexoft.leftmenu.PageFragment;

public class SettingsFragment extends PageFragment {

	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.settings_fragment, null);
	
		setTitleText(R.string.settings_title);
		
		return view;
	}
}
