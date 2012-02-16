package com.srcology.android.thetvdb.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.favorites.Favorites;

public class FavoriteDAO extends TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	public static SQLiteDatabase mDatabase;
	private String mFavoriteTable;	

	public FavoriteDAO(Context context) {
		mContext = context;
		String dbName = mContext.getString(R.string.db_name);
		mFavoriteTable = mContext.getString(R.string.favorite_table);
		Log.d(TAG, new StringBuilder("Open or create database: ")
			.append(dbName)
			.toString());
		mDatabase = mContext.openOrCreateDatabase(dbName, 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable();
	}

	public void createTable() {
		String createLanguage = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
			.append(mFavoriteTable)
			.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("tvdbId INTEGER, showName TEXT);")
			.toString();
		createTable(mDatabase, 
				createLanguage);
	}

	public void delete(String tvdbId) {
		delete(mDatabase, 
				mFavoriteTable, 
				"tvdbId=?", 
				new String[]{tvdbId});
	}

	public void deleteAll() {
		delete(mDatabase, 
				mFavoriteTable, 
				null, 
				null);
	}

	public void insert(Favorites favorites) {
		ArrayList<String> series = favorites.series;
		for (String tvdbId: series) {
			ContentValues values = new ContentValues();
			values.put("tvdbId", tvdbId);
			insert(mDatabase, 
					mFavoriteTable,
					"", 
					values);
		}
	}
	
	public void addShowNameToFavorite(String showId, String showName) {
		ContentValues values = new ContentValues();
		values.put("showName", showName);
	    update(mDatabase, 
	    		mFavoriteTable, 
	    		values, 
				"tvdbId=?", 
				new String[]{showId});
	}

	public Favorites findAllFavorites() {
		Cursor cursor = findAll();
		return convertCursorToFavorites(cursor);
	}

	public Cursor findAll() {
		Cursor cursor = query(mDatabase, 
				mFavoriteTable, 
				new String[]{"_id", "tvdbId", "showName"},  
				null, null, null, null, 
				"showName");
		convertCursorToFavorites(cursor);
		return cursor;
	}

	public Cursor findAllWithNames() {
		Cursor cursor = query(mDatabase, 
				mFavoriteTable, 
				new String[]{"_id", "tvdbId", "showName"},  
				"showName IS NOT NULL", 
				null, null, null, 
				"showName");
		convertCursorToFavorites(cursor);
		return cursor;
	}
	
	public Favorites convertCursorToFavorites(Cursor cursor) {
		Favorites favorites = new Favorites();
		ArrayList<String> seriesIds = new ArrayList<String>();
		ArrayList<String> seriesNames = new ArrayList<String>();
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				seriesIds.add(cursor.getString(cursor.getColumnIndex("tvdbId")));
				seriesNames.add(cursor.getString(cursor.getColumnIndex("showName")));
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		favorites.series = seriesIds;
		favorites.seriesNames = seriesNames;
		return favorites;
	}
}
