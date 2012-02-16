package com.srcology.android.thetvdb.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.newtoday.Item;

public class TodayDAO extends TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	public static SQLiteDatabase mDatabase;
	private String mTodayTable;	

	public TodayDAO(Context context) {
		mContext = context;
		String dbName = mContext.getString(R.string.db_name);
		mTodayTable = mContext.getString(R.string.today_table);
		Log.d(TAG, new StringBuilder("Open or create database: ")
			.append(dbName)
			.toString());
		mDatabase = mContext.openOrCreateDatabase(dbName, 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable();
	}

	public void createTable() {
		String createLanguage = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
			.append(mTodayTable)
			.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("showId INTEGER, showName TEXT, episodeId INTEGER, ")
			.append("episodeName TEXT, seasonId INTEGER, link TEXT, description TEXT);")
			.toString();
		createTable(mDatabase, 
				createLanguage);
	}

	public void delete(Item item) {
		String episodeId = item.episodeId;
		delete(mDatabase, 
				mTodayTable, 
				"episodeId=?", 
				new String[]{episodeId});
	}

	public void deleteAll() {
		delete(mDatabase, 
				mTodayTable, 
				null, 
				null);
	}

	public void insert(Item item) {
		ContentValues values = new ContentValues();
		values.put("showId", item.showId);
		values.put("showName", item.showName);
		values.put("episodeId", item.episodeId);
		values.put("episodeName", item.episodeName);
		values.put("seasonId", item.seasonId);
		values.put("link", item.link);
		values.put("description", item.description);
		insert(mDatabase, 
				mTodayTable,
				"", 
				values);
	}

	public Item findByEpisodeId(String episodeId) {
		Cursor cursor = query(mDatabase, 
				mTodayTable, 
				new String[]{"_id", "showId", "showName", "episodeId", "episodeName", "seasonId", "link", "description"}, 
				"episodeId=?", 
				new String[]{episodeId}, 
				null, null, null);
		ArrayList<Item> itemList = convertCursorToItem(cursor);
		return itemList.get(0);
	}

	public ArrayList<Item> findAllItems() {
		Cursor cursor = findAll();
		return convertCursorToItem(cursor);
	}

	public Cursor findAll() {
		Cursor cursor = query(mDatabase, 
				mTodayTable, 
				new String[]{"_id", "showId", "showName", "episodeId", "episodeName", "seasonId", "link", "description"},  
				null, null, null, null, null);
		return cursor;
	}
	
	public ArrayList<Item> convertCursorToItem(Cursor cursor) {
		ArrayList<Item> items = new ArrayList<Item>();
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Item item = new Item();
				item.showId = cursor.getString(cursor.getColumnIndex("showId"));
				item.showName = cursor.getString(cursor.getColumnIndex("showName"));
				item.episodeId = cursor.getString(cursor.getColumnIndex("episodeId"));
				item.episodeName = cursor.getString(cursor.getColumnIndex("episodeName"));
				item.seasonId = cursor.getString(cursor.getColumnIndex("seasonId"));
				item.link = cursor.getString(cursor.getColumnIndex("link"));
				item.description = cursor.getString(cursor.getColumnIndex("description"));
				items.add(item);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		return items;
	}
}
