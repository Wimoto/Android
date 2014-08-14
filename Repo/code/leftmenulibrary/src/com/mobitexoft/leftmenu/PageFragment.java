package com.mobitexoft.leftmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.R;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class PageFragment extends com.mobitexoft.navigation.PageFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
			
		View menuControlView = inflater.inflate(R.layout.header_menu_view, null);
		menuControlView.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				((SlidingFragmentActivity)getActivity()).toggle();
			}
		});
		setLeftControlView(menuControlView);
		
		return view;
	}
	
}
