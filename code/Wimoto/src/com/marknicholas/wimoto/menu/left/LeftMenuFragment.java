package com.marknicholas.wimoto.menu.left;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.screens.help.HelpFragment;
import com.marknicholas.wimoto.screens.searchsensor.SearchSensorFragment;
import com.marknicholas.wimoto.screens.settings.SettingsFragment;
import com.mobitexoft.navigation.NavigationFragment;

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
		
		mLeftMenuAdapter = new LeftMenuAdapter();
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
			mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new SearchSensorFragment()), menuItemTag);
		} else if (MenuItem.SETTINGS.equals(menuItemTag)) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new SettingsFragment()), menuItemTag);
		} else if (MenuItem.HELP.equals(menuItemTag)) {
			mSlidingFragmentActivity.pushFragmentToCenterSlide(NavigationFragment.newInstance(new HelpFragment()), menuItemTag);
		}
	}

}
