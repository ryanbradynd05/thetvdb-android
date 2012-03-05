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

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.support.v4.app.SherlockPreferenceActivity;
import android.support.v4.view.MenuItem;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;

public class SettingsActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String TAG = TvdbApp.TAG;
	private EditTextPreference mAccountPreference;
	private ListPreference mFrequencyPreference;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "Start onCreate in SettingsActivity");
        super.onCreate(savedInstanceState);
        TvdbApp.applySharedTheme(this.getApplicationContext(), this);
        addPreferencesFromResource(R.xml.preferences);

		updateAccountPref();
        updateFrequencyPref();
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
    }

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Let's do something a preference value changes
		if (key.equalsIgnoreCase(getString(R.string.pref_account_id))) {
			updateAccountPref();	
		} else if (key.equalsIgnoreCase(getString(R.string.pref_update_freq))) {
			updateFrequencyPref();	
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
        case android.R.id.home:
            // App icon in action bar clicked; go home
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
		case R.id.menu_search:
			onSearchRequested();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void updateAccountPref() {
        mAccountPreference = (EditTextPreference) findPreference(getString(R.string.pref_account_id));
        String accountId = getPreferenceScreen().getSharedPreferences().getString(getString(R.string.pref_account_id), "");
        mAccountPreference.setSummary(accountId);
	}
	
	private void updateFrequencyPref() {
		// Launch
		mFrequencyPreference = (ListPreference) findPreference(getString(R.string.pref_update_freq));
		mFrequencyPreference.setSummary(getPreferenceScreen().getSharedPreferences().getString(getString(R.string.pref_update_freq), ""));
	}
}
