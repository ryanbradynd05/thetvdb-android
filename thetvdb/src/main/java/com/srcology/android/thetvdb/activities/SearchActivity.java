package com.srcology.android.thetvdb.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.fragments.SearchFragment;
import com.srcology.android.thetvdb.util.SearchSuggestionProvider;

public class SearchActivity extends FragmentActivity  implements SearchFragment.SearchLoadListener {
	private static final String TAG = TvdbApp.TAG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Start onCreate in SearchActivity");
		super.onCreate(savedInstanceState);
        TvdbApp.applySharedTheme(this.getApplicationContext(), this);
		setContentView(R.layout.search);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
					SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);
			SearchFragment searchFragment = 
					(SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search);
			searchFragment.search(query);
		}
	}
    
    private void showProgress(boolean completed) {
		if (completed) {
			setProgressBarIndeterminateVisibility(Boolean.FALSE);
		} else {
			setProgressBarIndeterminateVisibility(Boolean.TRUE);
		}
    }

	public void onSearchLoad(boolean completed) {
		Log.d(TAG,"onSearchLoad: " + completed);
		showProgress(completed);
	}
}
