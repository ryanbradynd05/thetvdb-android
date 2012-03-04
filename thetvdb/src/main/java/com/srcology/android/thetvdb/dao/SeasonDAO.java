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
import com.srcology.android.thetvdb.model.xml.show.Season;

public class SeasonDAO extends TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	public static SQLiteDatabase mDatabase;
	private String mSeasonTable;	

	public SeasonDAO(Context context) {
		mContext = context;
		String dbName = mContext.getString(R.string.db_name);
		mSeasonTable = mContext.getString(R.string.season_table);
		Log.d(TAG, new StringBuilder("Open or create database: ")
			.append(dbName)
			.toString());
		mDatabase = mContext.openOrCreateDatabase(dbName, 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable();
	}

	public void createTable() {
		String createLanguage = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
			.append(mSeasonTable)
			.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("seriesId INTEGER, seasonId INTEGER, seasonNumber INTEGER);")
			.toString();
		createTable(mDatabase, 
				createLanguage);
	}

	public void delete(String seasonId) {
		delete(mDatabase, 
				mSeasonTable, 
				"seasonId=?", 
				new String[]{seasonId});
	}

	public void deleteBySeries(String seriesId) {
		delete(mDatabase, 
				mSeasonTable, 
				"seriesId=?", 
				new String[]{seriesId});
	}

	public void deleteAll() {
		delete(mDatabase, 
				mSeasonTable, 
				null, 
				null);
	}

	public void insert(Season season) {
		ContentValues values = new ContentValues();
		values.put("seasonId", season.seasonId);
		values.put("seriesId", season.seriesId);
		values.put("seasonNumber", season.seasonNumber);
		insert(mDatabase, 
				mSeasonTable,
				"", 
				values);
	}

	public Season findSeason(String seasonId) {
		Cursor cursor = query(mDatabase, 
				mSeasonTable, 
				new String[]{"_id","seasonId","seriesId","seasonNumber"}, 
				"seasonId=?", 
				new String[]{seasonId}, 
				null, null, 
				"seasonNumber");
		ArrayList<Season> seasons = convertCursorToSeason(cursor);
		return seasons.get(0);
	}

	public Season findSeason(String seasonId, String seasonNumber) {
		Cursor cursor = query(mDatabase, 
				mSeasonTable, 
				new String[]{"_id","seasonId","seriesId","seasonNumber"}, 
				"seasonId=? AND seasonNumber=?", 
				new String[]{seasonId,seasonNumber}, 
				null, null, 
				"seasonNumber");
		ArrayList<Season> seasons = convertCursorToSeason(cursor);
		return seasons.get(0);
	}

	public ArrayList<Season> findBySeriesId(String seriesId) {
		Cursor cursor = query(mDatabase, 
				mSeasonTable, 
				new String[]{"_id","seasonId","seriesId","seasonNumber"}, 
				"seriesId=?", 
				new String[]{seriesId}, 
				null, null,  
				"seasonNumber");
		ArrayList<Season> seasons = convertCursorToSeason(cursor);
		return seasons;
	}
	
	public ArrayList<Season> convertCursorToSeason(Cursor cursor) {
		ArrayList<Season> seasons = new ArrayList<Season>();
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Season season = new Season();
				season.seasonId = cursor.getString(cursor.getColumnIndex("seasonId"));
				season.seriesId = cursor.getString(cursor.getColumnIndex("seriesId"));
				season.seasonNumber = cursor.getString(cursor.getColumnIndex("seasonNumber"));
				seasons.add(season);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		return seasons;
	}
}
