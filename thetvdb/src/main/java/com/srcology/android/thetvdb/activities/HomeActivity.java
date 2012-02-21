package com.srcology.android.thetvdb.activities;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBar;
import android.support.v4.app.ActionBar.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.view.Window;
import android.util.Log;
import android.view.MenuInflater;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.fragments.TodayFragment;

public class HomeActivity extends FragmentActivity implements TodayFragment.TodayLoadListener {
	private static final String TAG = TvdbApp.TAG;
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG,"onCreate in HomeActivity");
        super.onCreate(savedInstanceState);
        TvdbApp.applySharedTheme(this.getApplicationContext(), this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.home);
        
//        startService(new Intent(TvdbService.class.getName()));
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//        ActionBar.Tab favTab = getSupportActionBar().newTab().setText(getString(R.string.favorites));
        ActionBar.Tab todayTab = getSupportActionBar().newTab().setText(getString(R.string.today));

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, getSupportActionBar(), mViewPager);
//        mTabsAdapter.addTab(favTab, FavoritesFragment.class);
        mTabsAdapter.addTab(todayTab, TodayFragment.class);

        if (savedInstanceState != null) {
        	getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("index"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", getSupportActionBar().getSelectedNavigationIndex());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
        case android.R.id.home:
            // App icon in action bar clicked; go home
            // Do nothing on home screen
            return false;
		case R.id.menu_search:
			onSearchRequested();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    public static class TabsAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener, ActionBar.TabListener {
        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<String> mTabs = new ArrayList<String>();

        public TabsAdapter(FragmentActivity activity, ActionBar actionBar, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = actionBar;
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss) {
            mTabs.add(clss.getName());
            mActionBar.addTab(tab.setTabListener(this));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(mContext, mTabs.get(position), null);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }

        public void onPageScrollStateChanged(int state) {
        }

    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		mViewPager.setCurrentItem(tab.getPosition());
    	}

    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
    	}

    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    	}
    }
    
    private void showProgress(boolean completed) {
		if (completed) {
			setProgressBarIndeterminateVisibility(Boolean.FALSE);
		} else {
			setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}
    }

	public void onTodayLoad(boolean completed) {
		Log.d(TAG,"onTodayLoad: " + completed);
		showProgress(completed);
	}
}