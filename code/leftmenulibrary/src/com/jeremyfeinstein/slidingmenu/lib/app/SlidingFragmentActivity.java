package com.jeremyfeinstein.slidingmenu.lib.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.jeremyfeinstein.slidingmenu.lib.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SlidingFragmentActivity extends FragmentActivity implements SlidingActivityBase {

	private SlidingActivityHelper mHelper;

	private SlidingCenterFragment mCenterFragment;
	private Fragment mLeftMenuFragment;
	private Fragment mRightMenuFragment;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
		
        setContentView(R.layout.activity_sliding);
        setBehindContentView(R.layout.left_menu_frame);
        setSecondaryMenu(R.layout.right_menu_frame);
        
        mCenterFragment = new SlidingCenterFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.centerFragmentContainer, mCenterFragment).commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (mCenterFragment != null) {
			mCenterFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		if (!mCenterFragment.popFragment()) {
			super.onBackPressed();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#findViewById(int)
	 */
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mHelper.findViewById(id);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View v, LayoutParams params) {
		super.setContentView(v, params);
		mHelper.registerAboveContentView(v, params);
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(int)
	 */
	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View)
	 */
	public void setBehindContentView(View v) {
		setBehindContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	public void setBehindContentView(View v, LayoutParams params) {
		mHelper.setBehindContentView(v, params);
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu()
	 */
	private SlidingMenu getSlidingMenu() {
		return mHelper.getSlidingMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#toggle()
	 */
	public void toggle() {
		mHelper.toggle();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showAbove()
	 */
	public void showContent() {
		mHelper.showContent();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showBehind()
	 */
	public void showMenu() {
		mHelper.showMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu()
	 */
	public void showSecondaryMenu() {
		mHelper.showSecondaryMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setSlidingActionBarEnabled(boolean)
	 */
	public void setSlidingActionBarEnabled(boolean b) {
		mHelper.setSlidingActionBarEnabled(b);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}

	protected void setLeftMenuFragment(Fragment leftMenuFragment) {
		mLeftMenuFragment = leftMenuFragment;
		
        getSupportFragmentManager().beginTransaction().replace(R.id.menuFragmentContainer, mLeftMenuFragment).commit();
	}
	
	protected void setRightMenuFragment(Fragment rightMenuFragment) {
		mRightMenuFragment = rightMenuFragment;
		
        getSupportFragmentManager().beginTransaction().replace(R.id.secondaryMenuFragmentContainer, mRightMenuFragment).commit();
	}
	
	public void pushFragmentToCenterSlide(Fragment fragment, String tag) {
		pushFragmentToCenterSlide(fragment, tag, false);
	}
	
	public void pushFragmentToCenterSlide(Fragment fragment, String tag, boolean cache) {
		mCenterFragment.pushFragment(fragment, tag, cache);
		getSlidingMenu().showContent();
	}
	
	protected void setBehindOffsetRes(int resId) {
		getSlidingMenu().setBehindOffsetRes(resId);
	}
	
	protected void setFadeDegree(float degree) {
		getSlidingMenu().setFadeDegree(degree);
	}
	
	protected void setTouchModeAbove(int touchMode) {
		getSlidingMenu().setTouchModeAbove(touchMode);
	}
	
	protected void setShadowWidthRes(int resId) {
	    getSlidingMenu().setShadowWidthRes(resId);
	}
	
	protected void setShadowDrawable(int resId) {
		getSlidingMenu().setShadowDrawable(resId);
	}
	
	protected void setSecondaryShadowDrawable(int resId) {
		getSlidingMenu().setSecondaryShadowDrawable(resId);
	}
	
	protected void setMenuMode (int menuMode) {
		getSlidingMenu().setMode(menuMode);
	}

	private void setSecondaryMenu(int id) {
		getSlidingMenu().setSecondaryMenu(id);
	}
}
