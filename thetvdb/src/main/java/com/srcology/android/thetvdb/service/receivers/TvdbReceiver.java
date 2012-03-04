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
