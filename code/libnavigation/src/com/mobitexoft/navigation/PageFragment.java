package com.mobitexoft.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {

	private TextView mTitleTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		View backControlView = inflater.inflate(R.layout.header_back_view, null);
		backControlView.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				onBackTapped();
			}
		});
		setBackControlView(backControlView);
			
		View titleView = inflater.inflate(R.layout.header_title_view, null);
		setTitleView(titleView);
		
		mTitleTextView = (TextView) titleView.findViewById(R.id.titleTextView);
		
		return view;
	}
	
	public void pushFragment(PageFragment fragment) {
		Fragment parentFragment = getParentFragment().getParentFragment();
		((NavigationFragment)parentFragment).pushFragment(fragment);
	}	
	
	public void setHeaderVisibility(int visibility) {
		SlideFragment parentFragment = (SlideFragment)getParentFragment();
		parentFragment.setHeaderVisibility(visibility);
	}
	
	public void setBackControlView(View view) {
		SlideFragment parentFragment = (SlideFragment)getParentFragment();
		parentFragment.setBackControlView(view);
	}
	
	public void setLeftControlView(View view) {
		SlideFragment parentFragment = (SlideFragment)getParentFragment();
		parentFragment.setLeftControlView(view);
	}
		
	public void setTitleView(View view) {
		SlideFragment parentFragment = (SlideFragment)getParentFragment();
		parentFragment.setTitleView(view);
	}
	
	protected void setTitleText(int res) {
		mTitleTextView.setText(res);
	}
	
	protected void setTitleText(String text) {
		mTitleTextView.setText(text);
	}
	
	public void setRightControlView(View view) {
		SlideFragment parentFragment = (SlideFragment)getParentFragment();
		parentFragment.setRightControlView(view);
	}
	
	protected void onBackTapped() {
		Fragment parentFragment = getParentFragment().getParentFragment();
		((NavigationFragment)parentFragment).popFragment();
	}
}
