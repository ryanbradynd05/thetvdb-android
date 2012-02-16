package com.srcology.android.thetvdb.fragments;

import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.SupportActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.newtoday.Channel;
import com.srcology.android.thetvdb.model.xml.newtoday.Item;
import com.srcology.android.thetvdb.model.xml.newtoday.Rss;
import com.srcology.android.thetvdb.util.TvdbDownloader;

public class TodayFragment extends ListFragment {
	private static final String TAG = TvdbApp.TAG;
	private TvdbDownloader mTvdbDownloader;
	private SharedPreferences mSharedPrefs;
	private TodayTask mTask;
	private Cursor mData;
	private TodayLoadListener loadListener;
//	private OnSeriesSelectListener seriesListener;
//	private OnEpisodeSelectListener episodeListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Start onCreate in TodayFragment");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		mTvdbDownloader = new TvdbDownloader(getActivity().getApplicationContext());
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (mTask != null) {
			if (mTask.getStatus() == Status.FINISHED) {
				//Data is already loaded. Show it
				updateDisplay(mData, getActivity());
			}
			//Task still running
			return;
		}

		//Start work in background
		mTask = new TodayTask(getActivity(), false);
		mTask.execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_today, container, false);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
    public void onAttach(SupportActivity activity) {
        super.onAttach(activity);
        try {
        	loadListener = (TodayLoadListener) activity;
//        	seriesListener = (OnSeriesSelectListener) activity;
//        	episodeListener = (OnEpisodeSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                " must implement OnTodayLoadListener");
        }
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.refresh_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1,
					new String[]{});
			setListAdapter(adapter);
			new TodayTask(getActivity(), true).execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//		try {
//			super.onListItemClick(l, v, position, id);
//			Cursor cursor = (Cursor) l.getItemAtPosition(position);
//			if (cursor.getCount() > 0) {
//				cursor.moveToPosition(position);
//				int showIdIndex = cursor.getColumnIndex("showId");
//				Item item = new Item();
//				String showId = cursor.getString(showIdIndex);
//				item.showId = showId;
//				String strTextToDisplay = "Selected item is : " + showId;
//				Toast.makeText(getActivity(), strTextToDisplay, Toast.LENGTH_LONG).show();
//			}
//		} catch(Exception ex) {
//			Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
//		}
//	}
	
	public interface TodayLoadListener {
        public void onTodayLoad(boolean completed);
    }
	
//	public interface OnSeriesSelectListener {
//        public void onSeriesSelect(String seriesId);
//    }
//	
//	public interface OnEpisodeSelectListener {
//        public void onEpisodeSelect(String episodeId);
//    }
	
	private class TodayTask extends AsyncTask<String, Void, Void> {
		private Cursor smData;
		private Activity smActivity;
		private boolean smForceUpdate;
		
		public TodayTask(Activity activity, boolean forceUpdate) {
	        super();
	        smActivity = activity;
	        smForceUpdate = forceUpdate;
	    }

		protected void onPreExecute() {
			loadListener.onTodayLoad(false);
		}

		protected Void doInBackground(final String... args) {
			if (smForceUpdate) {
				smData = loadEpisodesFromTvdb();
			} else {
				if (!updatedToday()) {
					smData = loadEpisodesFromTvdb();
				} else {
					smData = loadEpisodesFromDatabase();
				}
			}
			return null;
		}

		protected void onPostExecute(final Void unused) {
			updateDisplay(smData, smActivity);
			registerForContextMenu(getListView());
			loadListener.onTodayLoad(true);
		}
	}
	
	private void updateDisplay(Cursor data, Activity activity) {
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity, 
				android.R.layout.simple_list_item_2,
				data,
				new String[] {"showName", "episodeName"},
				new int[] {android.R.id.text1, android.R.id.text2},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}
	
	private String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-12"));
		return sdf.format(new Date());		
	}
	
	private boolean updatedToday() {
		String todayAccessDate = mSharedPrefs.getString(getString(R.string.pref_access_today), "");
		String currentDate = getCurrentDate();
		Log.d(TAG,"currentDate: " + currentDate);
		if (todayAccessDate.equalsIgnoreCase(currentDate)) {
			Log.d(TAG,"already updated today");
			return true;
		} else {
			Log.d(TAG,"not yet updated today");
			return false;
		}
	}
	
	private Cursor loadEpisodesFromDatabase() {
		Log.d(TAG,"loadEpisodesFromDatabase");
		Cursor data = TvdbApp.todayDAO.findAll();
		return data;
	}
	
	private Cursor loadEpisodesFromTvdb() {
		Log.d(TAG,"loadEpisodesFromTvdb");
		String nowPlayingUrl = new StringBuilder(getString(R.string.mirror_path))
			.append("rss/newtoday.php")
			.toString();
		String nowPlaying = mTvdbDownloader.getXmlString(nowPlayingUrl);  
		Reader reader = new StringReader(nowPlaying);
		addEpisodesToDatabase(reader);
		Cursor data = loadEpisodesFromDatabase();
		return data;
	}
	
	private void addEpisodesToDatabase(Reader reader) {
		Log.d(TAG, "addEpisodesToDatabase");
		TvdbApp.todayDAO.deleteAll();
		Serializer serializer = new Persister();
		try {
			Rss rss = serializer.read(Rss.class, reader, false);
			Channel channel = rss.channel;
			ArrayList<Item> items = channel.items;
			for (Item item : items) {
				String title = item.title;
				int lastColon = title.lastIndexOf(":");
				String showName = title.substring(0, lastColon);
				String episodeName = title.substring(lastColon + 2);
				item.showName = showName;
				item.episodeName = episodeName;
				
				String link = item.link;
				String patternStr = "seriesid=(\\d+).+seasonid=(\\d+).+id=(\\d+)";
				Pattern pattern = Pattern.compile(patternStr);
				Matcher matcher = pattern.matcher(link);
				boolean matchFound = matcher.find();
				if (matchFound) {
					item.showId = matcher.group(1);
					item.seasonId = matcher.group(2);
					item.episodeId = matcher.group(3);
				}
				TvdbApp.todayDAO.insert(item);
			}
			SharedPreferences.Editor editor = mSharedPrefs.edit();
			editor.putString(getString(R.string.pref_access_today), getCurrentDate());
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
