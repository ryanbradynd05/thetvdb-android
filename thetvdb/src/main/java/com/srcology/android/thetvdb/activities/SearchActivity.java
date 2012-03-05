/**
 * Copyright (C) 2012 Ryan Brady <rbrady@srcology.com>
 *
 * 		TheTVDB-Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.srcology.android.thetvdb.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Window;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.fragments.SearchFragment;
import com.srcology.android.thetvdb.service.TvdbService;
import com.srcology.android.thetvdb.util.SearchSuggestionProvider;

public class SearchActivity extends FragmentActivity implements SearchFragment.SearchLoadListener {
	private static final String TAG = TvdbApp.TAG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Start onCreate in SearchActivity");
		super.onCreate(savedInstanceState);
        TvdbApp.applySharedTheme(this.getApplicationContext(), this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.search);

        startService(new Intent(TvdbService.class.getName()));
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
