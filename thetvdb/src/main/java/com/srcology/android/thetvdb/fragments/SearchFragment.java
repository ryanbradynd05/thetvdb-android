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
package com.srcology.android.thetvdb.fragments;

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.SupportActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.activities.HomeActivity;
import com.srcology.android.thetvdb.adapters.SearchAdapter;
import com.srcology.android.thetvdb.model.xml.search.Data;
import com.srcology.android.thetvdb.model.xml.search.Series;
import com.srcology.android.thetvdb.util.TvdbDownloader;

public class SearchFragment extends ListFragment {
	private static final String TAG = TvdbApp.TAG;
	private TvdbDownloader mTvdbDownloader;
	private SearchLoadListener loadListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Start onCreate in TvdbSearchActivity");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		mTvdbDownloader = new TvdbDownloader(getActivity().getApplicationContext());
	}
	
	@Override
    public void onAttach(SupportActivity activity) {
        super.onAttach(activity);
        try {
        	loadListener = (SearchLoadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                " must implement OnSearchLoadListener");
        }
    }

	public void search(String query) {
		new SearchTask(getActivity(), query).execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, container, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.home_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
        case android.R.id.home:
            // App icon in action bar clicked; go home
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
		case R.id.menu_search:
			getActivity().onSearchRequested();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public interface SearchLoadListener {
        public void onSearchLoad(boolean completed);
    }

	private class SearchTask extends AsyncTask<String, Void, Void> {
		private ArrayList<Series> smSeriesList;
		private Activity smActivity;
		private String smQuery;

		public SearchTask(Activity activity, String query) {
			super();
			smActivity = activity;
			smQuery = query;
		}

		protected void onPreExecute() {
			loadListener.onSearchLoad(false);
		}

		protected Void doInBackground(final String... args) {
			smSeriesList = searchTvdb(smQuery);
			return null;
		}

		protected void onPostExecute(final Void unused) {
			if (smActivity != null) {
				SearchAdapter adapter = new SearchAdapter(smActivity.getApplicationContext(), 
						android.R.layout.simple_list_item_1, smSeriesList);
				setListAdapter(adapter);
				registerForContextMenu(getListView());
				loadListener.onSearchLoad(true);
			}
		}
	}

	private ArrayList<Series> searchTvdb(String query) {
		ArrayList<Series> seriesResults = new ArrayList<Series>();

		StringBuilder nowPlayingUrl = new StringBuilder();
		try {
			nowPlayingUrl.append(getString(R.string.api_path))
			.append("GetSeries.php?seriesname=")
			.append(URLEncoder.encode(query, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String nowPlaying = mTvdbDownloader.getXmlString(nowPlayingUrl.toString());  
		Serializer serializer = new Persister();  
		Reader reader = new StringReader(nowPlaying);
		Data data;
		try {
			data = serializer.read(Data.class, reader, false);
			seriesResults = data.series;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seriesResults;
	}
}
