package com.marknicholas.wimoto.menu.left;

import java.util.ArrayList;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.utils.AppContext;

public class LeftMenuAdapter extends BaseAdapter {

	private ArrayList<MenuItem> mMenuItems;
	
	private Resources mResources;
	
	public LeftMenuAdapter() {
		mResources = AppContext.getContext().getResources();
		
		initMenuItems();
	}
	
	private void initMenuItems() {
		mMenuItems = new ArrayList<MenuItem> ();
		
		mMenuItems.add(new MenuItem(mResources.getString(R.string.left_menu_add_sensor), MenuItem.SEARCH));
		mMenuItems.add(new MenuItem(mResources.getString(R.string.left_menu_settings), MenuItem.SETTINGS));
		mMenuItems.add(new MenuItem(mResources.getString(R.string.left_menu_help), MenuItem.HELP));
	}
	
	@Override
	public int getCount() {
		return mMenuItems.size();
	}

	@Override
	public MenuItem getItem(int position) {
		return mMenuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuItem item = getItem(position);
		if (convertView == null) {
			convertView = new LeftMenuView(AppContext.getContext());
		}
		((LeftMenuView)convertView).setMenuItem(item);
		
		return convertView;
	}

}
