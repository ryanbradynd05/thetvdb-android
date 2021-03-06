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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.srcology.android.thetvdb.TvdbApp;

public abstract class TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	
	public synchronized void createTable(SQLiteDatabase database, String sql) {
		Log.v(TAG, "Create table");
		try {
			database.beginTransaction();
			database.execSQL(sql);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public Cursor query(SQLiteDatabase database, String table, String[] columns, String selection ,String [] selectionArgs, String groupBy, String having, String orderBy) {
		Cursor cursor = null;
		try {
			database.beginTransaction();
			cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
		return cursor;
	}
	
	public synchronized void delete(SQLiteDatabase database, String table, String whereClause, String[] whereArgs) {
		try {
			database.beginTransaction();
			database.delete(table, whereClause, whereArgs);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
	
	public synchronized void insert(SQLiteDatabase database, String table, String nullColumnHack, ContentValues values) {
		try {
			database.beginTransaction();
			database.insert(table, nullColumnHack, values);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
	
	public synchronized void insertMultiple(SQLiteDatabase database, String table, String nullColumnHack, ArrayList<ContentValues> values) {
		try {
			database.beginTransaction();
			for (ContentValues currentValues : values) {
				database.insert(table, nullColumnHack, currentValues);
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
	
	public synchronized void update(SQLiteDatabase database, String table, ContentValues values, String whereClause, String[] whereArgs) {
		try {
			database.beginTransaction();
			database.update(table, values, whereClause, whereArgs);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
}