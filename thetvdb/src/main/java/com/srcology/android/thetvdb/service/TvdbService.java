package com.srcology.android.thetvdb.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.newtoday.Channel;
import com.srcology.android.thetvdb.model.xml.newtoday.Item;
import com.srcology.android.thetvdb.model.xml.newtoday.Rss;
import com.srcology.android.thetvdb.service.receivers.TvdbReceiver;
import com.srcology.android.thetvdb.util.TvdbDownloader;

public class TvdbService extends WakefulIntentService {
	private static final String TAG = TvdbApp.TAG;
    public static final String ACTION_ALL = "com.srcology.android.thetvdb.ACTION_ALL";
    public static final String ACTION_FAV = "com.srcology.android.thetvdb.ACTION_FAV";
    public static final String ACTION_TODAY = "com.srcology.android.thetvdb.ACTION_TODAY";
    public static final String ACTION_BROADCAST = "com.srcology.android.thetvdb.ACTION_BROADCAST";
    public static final String BROADCAST_STRING = "BROADCAST_STRING";
    public static final String FAV_COMPLETE = "FavComplete";
    public static final String TODAY_COMPLETE = "TodayComplete";

	public TvdbService() {
		super(TvdbService.class.getName());
	}

	@Override
	protected void doWakefulWork(Intent intent) {
        String action = intent.getAction();
        Log.i(TAG,"Do Wakeful Work " + action);
        
//        if (ACTION_ALL.equals(action)) {
//            Log.v(TAG, "TvdbService: ALL()");
//            TvdbService.scheduleRepeatingTask(this, intent);
//        } else if (ACTION_FAV.equals(action)) {
//            Log.v(TAG, "TvdbService: FAV()");
//            TvdbService.scheduleTask(this, intent);
//        } else if (ACTION_TODAY.equals(action)) {
//            Log.v(TAG, "TvdbService: TODAY()");
//            TvdbService.scheduleTask(this, intent);
//        } else {
//            Log.v(TAG, "TvdbService: default()");
//            TvdbService.scheduleRepeatingTask(this, action);
//        }
        if (ACTION_ALL.equals(action)) {   
        	loadEpisodesFromTvdb();
        } else if (ACTION_TODAY.equals(action)) {  
        	loadEpisodesFromTvdb();
        }
	}
	
	public static void scheduleTask(Context context, String action) {
        Log.i(TAG,"ScheduleTask: " + action);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent tvdbIntent = new Intent(context, TvdbReceiver.class);
		tvdbIntent.setAction(action);
		PendingIntent pendingTvdbIntent = PendingIntent.getBroadcast(context, 0, tvdbIntent,
			PendingIntent.FLAG_CANCEL_CURRENT);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
			SystemClock.elapsedRealtime()+500, 
			pendingTvdbIntent);
        Log.i(TAG,"Task Scheduled Once");
	}
	
	public static void scheduleRepeatingTask(Context context, String action) {
        Log.i(TAG,"ScheduleRepeatingTask: " + action);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent tvdbIntent = new Intent(context, TvdbReceiver.class);
		tvdbIntent.setAction(action);
		PendingIntent pendingTvdbIntent = PendingIntent.getBroadcast(context, 0, tvdbIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar alarmTime = Calendar.getInstance();
		alarmTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		alarmTime.set(Calendar.HOUR_OF_DAY, 9);
		alarmTime.set(Calendar.MINUTE, 0);
		Log.i(TAG,"AlarmTime: " + alarmTime.getTimeInMillis());
		Calendar now = Calendar.getInstance();
		Log.i(TAG,"Now: " + now.getTimeInMillis());
		long minsTillAlarm = (((alarmTime.getTimeInMillis() - now.getTimeInMillis()) / 1000) / 60);
		Log.i(TAG,"Mins Till Alarm: " + minsTillAlarm);
		am.setRepeating(AlarmManager.RTC_WAKEUP, 
			alarmTime.getTimeInMillis(), 
			AlarmManager.INTERVAL_DAY,
			pendingTvdbIntent);
        Log.i(TAG,"Task Scheduled Repeating");
	}
		
	private void loadEpisodesFromTvdb() {
		TvdbDownloader tvdbDownloader = new TvdbDownloader(getApplicationContext());
		Log.d(TAG,"loadEpisodesFromTvdb");
		String nowPlayingUrl = new StringBuilder(getString(R.string.mirror_path))
			.append("rss/newtoday.php")
			.toString();
		String nowPlaying = tvdbDownloader.getXmlString(nowPlayingUrl);  
		Reader reader = new StringReader(nowPlaying);
		addEpisodesToDatabase(reader);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, "addEpisodesToDatabase Completed");
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(TvdbService.ACTION_BROADCAST);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(BROADCAST_STRING, TODAY_COMPLETE);
		sendBroadcast(broadcastIntent);
	}
}
