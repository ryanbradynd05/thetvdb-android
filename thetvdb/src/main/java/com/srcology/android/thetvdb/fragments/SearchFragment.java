package com.srcology.android.thetvdb.fragments;

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Start onCreate in TvdbSearchActivity");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		mTvdbDownloader = new TvdbDownloader(getActivity().getApplicationContext());
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

	private class SearchTask extends AsyncTask<String, Void, Void> {
		private final ProgressDialog smDialog;
		private ArrayList<Series> smSeriesList;
		private Activity smActivity;
		private String smQuery;

		public SearchTask(Activity activity, String query) {
			super();
			smActivity = activity;
			smQuery = query;
			smDialog = new ProgressDialog(smActivity);
		}

		protected void onPreExecute() {
			smDialog.setMessage(getString(R.string.loading_text));
			smDialog.show();
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
				if (smDialog.isShowing()) {
					smDialog.dismiss();
				}
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
