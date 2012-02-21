package com.srcology.android.thetvdb.service.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.service.TvdbService;

public class TvdbReceiver extends BroadcastReceiver {
	private static final String TAG = TvdbApp.TAG;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "TvdbReceiver onReceive");
		intent.setClass(context, TvdbService.class);
		WakefulIntentService.sendWakefulWork(context, intent);
	}
}
