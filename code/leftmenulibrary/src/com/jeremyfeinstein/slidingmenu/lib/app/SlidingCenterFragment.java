package com.jeremyfeinstein.slidingmenu.lib.app;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.R;
import com.mobitexoft.navigation.NavigationFragment;

public class SlidingCenterFragment extends Fragment {
	
	private static String TAG = "SlidingCenterFragment";
	
	private Fragment mCurrentFragment;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center, container, false);
    }
	
	public void pushFragment(Fragment fragment, String tag, boolean cache) {
		if ((fragment == null) || ((tag == null) || (tag.isEmpty()))) {
			Log.e(TAG, "Can't push fragment due to fragment or tag is empty");
			return;
		}

		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		mCurrentFragment = fragmentManager.findFragmentByTag(tag);
		if ((mCurrentFragment == null) || (!cache)) {
			mCurrentFragment = fragment;
			fragmentTransaction.replace(R.id.centerFragmentContainer, mCurrentFragment, tag);
		} else {
			List<Fragment> fragments = fragmentManager.getFragments();
			
			for (Fragment f:fragments) {
				if (f.equals(mCurrentFragment)) {
					fragmentTransaction.show(f);
				} else {
					fragmentTransaction.hide(f);
				}
			}
		}

		fragmentTransaction.commit();
	}
	
	public boolean popFragment() {
		if ((mCurrentFragment != null) && (mCurrentFragment instanceof NavigationFragment)) {
			return ((NavigationFragment) mCurrentFragment).popFragment();
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mCurrentFragment != null) {
			mCurrentFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
}
