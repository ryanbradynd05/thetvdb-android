package com.srcology.android.thetvdb;

import java.io.File;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.srcology.android.thetvdb.dao.EpisodeDAO;
import com.srcology.android.thetvdb.dao.FavoriteDAO;
import com.srcology.android.thetvdb.dao.LanguageDAO;
import com.srcology.android.thetvdb.dao.SeasonDAO;
import com.srcology.android.thetvdb.dao.SeriesDAO;
import com.srcology.android.thetvdb.dao.TodayDAO;
import com.srcology.android.thetvdb.util.TvdbDownloader;

public class TvdbApp extends Application {
	public static final String TAG = "TvdbApp";
	public static LanguageDAO languageDAO;
	public static TodayDAO todayDAO;
	public static FavoriteDAO favoriteDAO;
	public static SeriesDAO seriesDAO;
	public static SeasonDAO seasonDAO;
	public static EpisodeDAO episodeDAO;

	@Override
	public void onCreate() {
		Log.d(TAG,"Start oncreate in TvdbApp");
	    super.onCreate();
	    Context context = getApplicationContext();
		
		languageDAO = new LanguageDAO(context);
		todayDAO = new TodayDAO(context);
		favoriteDAO = new FavoriteDAO(context);
		seriesDAO = new SeriesDAO(context);
		seasonDAO = new SeasonDAO(context);
		episodeDAO = new EpisodeDAO(context);
		
		TvdbDownloader tvdbDownloader = new TvdbDownloader(context);
		if (tvdbDownloader.isExternalStorageWriteable()) {
			String dataDirString = new StringBuilder(Environment.getExternalStorageDirectory().toString())
				.append(context.getString(R.string.data_dir))
				.toString();
			Log.d(TAG, "DataDir: " + dataDirString);
			if (verifyOrCreateDirectory(dataDirString)) {
				Log.d(TAG,"Create data directory");
			}
			
			String cacheDirString = new StringBuilder(Environment.getExternalStorageDirectory().toString())
				.append(context.getString(R.string.cache_dir))
				.toString();
			Log.d(TAG, "CacheDir: " + cacheDirString);
			if (verifyOrCreateDirectory(cacheDirString)) {
				Log.d(TAG,"Create cache directory");
			}
			
			String imagesDirString = new StringBuilder(Environment.getExternalStorageDirectory().toString())
				.append(context.getString(R.string.images_dir))
				.toString();
			Log.d(TAG, "ImagesDir: " + imagesDirString);
			if (verifyOrCreateDirectory(imagesDirString)) {
				Log.d(TAG,"Create images directory");
			}
			
			String bannersDirString = new StringBuilder(Environment.getExternalStorageDirectory().toString())
				.append(context.getString(R.string.banners_dir))
				.toString();
			Log.d(TAG, "BannersDir: " + bannersDirString);
			if (verifyOrCreateDirectory(bannersDirString)) {
				Log.d(TAG,"Create banners directory");
			}
			
			String episodesDirString = new StringBuilder(Environment.getExternalStorageDirectory().toString())
				.append(context.getString(R.string.episodes_dir))
				.toString();
			Log.d(TAG, "EpisodesDir: " + episodesDirString);
			if (verifyOrCreateDirectory(episodesDirString)) {
				Log.d(TAG,"Create episodes directory");
			}
		}
    }
	
	private boolean verifyOrCreateDirectory(String directoryPath) {
		File dataDir = new File(directoryPath);
		if(!dataDir.isDirectory()) {
			dataDir.mkdirs();
			return true;
		} else {
			return false;
		}
	}
	
	public static void deleteRecursively(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory()) {
	        for (File child : fileOrDirectory.listFiles()) {
	        	deleteRecursively(child);
	        }
	    }
	    fileOrDirectory.delete();
	}
	
	public static boolean notEmpty(String s) {
		return (s != null && s.length() > 0);
	}
	
	public static void applySharedTheme(Context context, Activity activty) {
        int darkThemeId = com.actionbarsherlock.R.style.Theme_Sherlock;
        activty.setTheme(darkThemeId);	
	}
}
