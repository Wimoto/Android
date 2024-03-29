package com.wimoto.app.menu.left;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.screens.help.HelpFragment;
import com.wimoto.app.screens.searchsensor.SearchSensorFragment;
import com.wimoto.app.screens.settings.SettingsFragment;

public class LeftMenuFragment extends ListFragment {
	
	private SlidingFragmentActivity mSlidingFragmentActivity;
	
	private LeftMenuAdapter mLeftMenuAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_left_fragment, null);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mSlidingFragmentActivity = (SlidingFragmentActivity)getActivity();
		
		mLeftMenuAdapter = new LeftMenuAdapter((AppContext) getActivity());
		setListAdapter(mLeftMenuAdapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		onMenuItemSelected(mLeftMenuAdapter.getItem(position).getTag());	
	}

	public void onMenuItemSelected(String menuItemTag) {
		if ((menuItemTag == null) || (menuItemTag.isEmpty())) {
			return;
		}
		
		if (MenuItem.SEARCH.equals(menuItemTag)) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(new SearchSensorFragment(), menuItemTag);
		} else if (MenuItem.SETTINGS.equals(menuItemTag)) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(new SettingsFragment(), menuItemTag);
		} else if (MenuItem.HELP.equals(menuItemTag)) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(new HelpFragment(), menuItemTag);
		}
	}

}
