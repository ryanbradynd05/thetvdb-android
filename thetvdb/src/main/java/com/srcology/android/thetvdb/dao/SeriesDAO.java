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
package com.srcology.android.thetvdb.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.show.Series;

public class SeriesDAO extends TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	public static SQLiteDatabase mDatabase;
	private String mSeriesTable;	

	public SeriesDAO(Context context) {
		mContext = context;
		String dbName = mContext.getString(R.string.db_name);
		mSeriesTable = mContext.getString(R.string.series_table);
		Log.d(TAG, new StringBuilder("Open or create database: ")
			.append(dbName)
			.toString());
		mDatabase = mContext.openOrCreateDatabase(dbName, 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable();
	}

	public void createTable() {
		String createLanguage = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
			.append(mSeriesTable)
			.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("tvdbId INTEGER, actors TEXT, airsDayOfWeek TEXT, airsTime TEXT, contentRating TEXT, firstAired TEXT, ")
			.append("genre TEXT, imdbId TEXT, language TEXT, network TEXT, networkId TEXT, overview TEXT, rating TEXT, ")
			.append("ratingCount TEXT, runtime TEXT, seriesId INTEGER, seriesName TEXT, status TEXT, added TEXT, addedBy TEXT, ")
			.append("banner TEXT, fanart TEXT, lastUpdated TEXT, poster TEXT, zap2itId TEXT);")
			.toString();
		createTable(mDatabase, 
				createLanguage);
	}

	public void delete(String tvdbId) {
		delete(mDatabase, 
				mSeriesTable, 
				"tvdbId=?", 
				new String[]{tvdbId});
	}

	public void deleteAll() {
		delete(mDatabase, 
				mSeriesTable, 
				null, 
				null);
	}

	public void insert(Series series) {		
		ContentValues values = new ContentValues();
		values.put("tvdbId", series.id);
		values.put("actors", series.Actors);
		values.put("airsDayOfWeek", series.Airs_DayOfWeek);
		values.put("airsTime", series.Airs_Time);
		values.put("contentRating", series.ContentRating);
		values.put("firstAired", series.FirstAired);
		values.put("genre", series.Genre);
		values.put("imdbId", series.IMDB_ID);
		values.put("language", series.Language);
		values.put("network", series.Network);
		values.put("networkId", series.NetworkID);
		values.put("overview", series.Overview);
		values.put("rating", series.Rating);
		values.put("ratingCount", series.RatingCount);
		values.put("runtime", series.Runtime);
		values.put("seriesId", series.SeriesID);
		values.put("seriesName", series.SeriesName);
		values.put("status", series.Status);
		values.put("added", series.added);
		values.put("addedBy", series.addedBy);
		values.put("banner", series.banner);
		values.put("fanart", series.fanart);
		values.put("lastUpdated", series.lastupdated);
		values.put("poster", series.poster);
		values.put("zap2itId", series.zap2it_id);		
		insert(mDatabase, 
				mSeriesTable,
				"", 
				values);
	}

	public Series find(String seriesId) {
		Log.d(TAG,"findBySeriesId");
		Cursor cursor = query(mDatabase, 
				mSeriesTable, 
				new String[]{"_id", "tvdbId", "actors", "airsDayOfWeek", "airsTime", "contentRating", "firstAired", "genre",
				"imdbId", "language", "network", "networkId", "overview", "rating", "ratingCount", "runtime", "seriesId",
				"seriesName", "status", "added", "addedBy", "banner", "fanart", "lastUpdated", "poster", "zap2itId"},
				"tvdbId=?", 
				new String[]{seriesId}, 
				null, null, null);
		ArrayList<Series> itemList = convertCursorToSeries(cursor);
		return itemList.get(0);
	}

	public ArrayList<Series> findAllItems() {
		Cursor cursor = findAll();
		return convertCursorToSeries(cursor);
	}

	public Cursor findAll() {
		Cursor cursor = query(mDatabase, 
				mSeriesTable, 
				new String[]{"_id", "tvdbId", "actors", "airsDayOfWeek", "airsTime", "contentRating", "firstAired", "genre",
				"imdbId", "language", "network", "networkId", "overview", "rating", "ratingCount", "runtime", "seriesId",
				"seriesName", "status", "added", "addedBy", "banner", "fanart", "lastUpdated", "poster", "zap2itId"},
				null, null, null, null, null);
		return cursor;
	}
	
	private ArrayList<Series> convertCursorToSeries(Cursor cursor) {
		Log.d(TAG,"convertCursorToSeries");
		ArrayList<Series> allSeries = new ArrayList<Series>();
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Series series = new Series();
				series.id = cursor.getString(cursor.getColumnIndex("tvdbId"));
				series.Actors = cursor.getString(cursor.getColumnIndex("actors"));
				series.Airs_DayOfWeek = cursor.getString(cursor.getColumnIndex("airsDayOfWeek"));
				series.Airs_Time = cursor.getString(cursor.getColumnIndex("airsTime"));
				series.ContentRating = cursor.getString(cursor.getColumnIndex("contentRating"));
				series.FirstAired = cursor.getString(cursor.getColumnIndex("firstAired"));
				series.Genre = cursor.getString(cursor.getColumnIndex("genre"));
				series.IMDB_ID = cursor.getString(cursor.getColumnIndex("imdbId"));
				series.Language = cursor.getString(cursor.getColumnIndex("language"));
				series.Network = cursor.getString(cursor.getColumnIndex("network"));
				series.NetworkID = cursor.getString(cursor.getColumnIndex("networkId"));
				series.Overview = cursor.getString(cursor.getColumnIndex("overview"));
				series.Rating = cursor.getString(cursor.getColumnIndex("rating"));
				series.RatingCount = cursor.getString(cursor.getColumnIndex("ratingCount"));
				series.Runtime = cursor.getString(cursor.getColumnIndex("runtime"));
				series.SeriesID = cursor.getString(cursor.getColumnIndex("seriesId"));
				series.SeriesName = cursor.getString(cursor.getColumnIndex("seriesName"));
				series.Status = cursor.getString(cursor.getColumnIndex("status"));
				series.added = cursor.getString(cursor.getColumnIndex("added"));
				series.addedBy = cursor.getString(cursor.getColumnIndex("addedBy"));
				series.banner = cursor.getString(cursor.getColumnIndex("banner"));
				series.fanart = cursor.getString(cursor.getColumnIndex("fanart"));
				series.lastupdated = cursor.getString(cursor.getColumnIndex("lastUpdated"));
				series.poster = cursor.getString(cursor.getColumnIndex("poster"));
				series.id = cursor.getString(cursor.getColumnIndex("zap2itId"));
				allSeries.add(series);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		return allSeries;
	}
}
