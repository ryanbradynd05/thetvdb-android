package com.srcology.android.thetvdb.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
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
import com.srcology.android.thetvdb.service.TvdbService;

public class TodayFragment extends ListFragment {
	private static final String TAG = TvdbApp.TAG;
	private TodayTask mTask;
	private Cursor mData;
	private TodayLoadListener loadListener;
	private TodayReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Start onCreate in TodayFragment");
		super.onCreate(savedInstanceState);
//		setRetainInstance(true);
		setHasOptionsMenu(true);
		
        IntentFilter filter = new IntentFilter(TvdbService.ACTION_BROADCAST);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new TodayReceiver();
        getActivity().registerReceiver(receiver, filter);
		
		if (mTask != null) {
			if (mTask.getStatus() == Status.FINISHED) {
				//Data is already loaded. Show it
				updateDisplay(mData, getActivity());
			}
			//Task still running
			return;
		}

		//Start work in background
		mTask = new TodayTask(getActivity());
		mTask.execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_today, container, false);
	}
	
	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
    public void onAttach(SupportActivity activity) {
        super.onAttach(activity);
        try {
        	loadListener = (TodayLoadListener) activity;
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
			new RefreshTask(getActivity()).execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public interface TodayLoadListener {
        public void onTodayLoad(boolean completed);
    }
	
	public class TodayReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String broadcastString = intent.getStringExtra(TvdbService.BROADCAST_STRING);
			Log.d(TAG,"BroadcastString: " + broadcastString);
			if (broadcastString.equalsIgnoreCase(TvdbService.TODAY_COMPLETE)) {
				Log.d(TAG,"Launch TodayTask");
				new TodayTask(getActivity()).execute();
			} else {
				Log.d(TAG,"Unknown BroadcastString");
			}
		}		
	}
	
	private class TodayTask extends AsyncTask<String, Void, Void> {
		private Cursor smData;
		private Activity smActivity;
		
		public TodayTask(Activity activity) {
	        super();
	        smActivity = activity;
	    }

		protected void onPreExecute() {
			loadListener.onTodayLoad(false);
		}

		protected Void doInBackground(final String... args) {
			smData = loadEpisodesFromDatabase();
			return null;
		}

		protected void onPostExecute(final Void unused) {
			updateDisplay(smData, smActivity);
			registerForContextMenu(getListView());
			loadListener.onTodayLoad(true);
		}
	}
	
	private class RefreshTask extends AsyncTask<String, Void, Void> {
		private Activity smActivity;
		
		public RefreshTask(Activity activity) {
	        super();
	        smActivity = activity;
	    }

		protected void onPreExecute() {
			loadListener.onTodayLoad(false);
		}

		protected Void doInBackground(final String... args) {
			TvdbService.scheduleTask(smActivity.getApplicationContext(), TvdbService.ACTION_TODAY);
			return null;
		}

		protected void onPostExecute(final Void unused) {}
	}
	
	private void updateDisplay(Cursor data, Activity activity) {
		if (activity != null) {
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(activity, 
					android.R.layout.simple_list_item_2,
					data,
					new String[] {"showName", "episodeName"},
					new int[] {android.R.id.text1, android.R.id.text2},
					CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			setListAdapter(adapter);
		}
	}
	
	private Cursor loadEpisodesFromDatabase() {
		Log.d(TAG,"loadEpisodesFromDatabase");
		Cursor data = TvdbApp.todayDAO.findAll();
		return data;
	}
}