package com.srcology.android.thetvdb.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.language.Language;

public class LanguageDAO extends TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	public static SQLiteDatabase mDatabase;
	private String mLanguageTable;	

	public LanguageDAO(Context context) {
		mContext = context;
		String dbName = mContext.getString(R.string.db_name);
		mLanguageTable = mContext.getString(R.string.language_table);
		Log.d(TAG, new StringBuilder("Open or create database: ")
			.append(dbName)
			.toString());
		mDatabase = mContext.openOrCreateDatabase(dbName, 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable();
	}

	public void createTable() {
		String createLanguage = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
			.append(mLanguageTable)
			.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("tvdbId INTEGER, name TEXT, abbreviation TEXT);")
			.toString();
		createTable(mDatabase, 
				createLanguage);
	}

	public void delete(Language language) {
		String abbreviation = language.abbreviation;
		delete(mDatabase, 
				mLanguageTable, 
				"abbreviation=?", 
				new String[]{abbreviation});
	}

	public void deleteAll() {
		delete(mDatabase, 
				mLanguageTable, 
				null, 
				null);
	}

	public void insert(Language language) {
		ContentValues values = new ContentValues();
		values.put("tvdbId", language.id);
		values.put("name", language.name);
		values.put("abbreviation", language.abbreviation);
		insert(mDatabase, 
				mLanguageTable,
				"", 
				values);
	}

	public Language findById(String tvdbId) {
		Cursor cursor = query(mDatabase, 
				mLanguageTable, 
				new String[]{"_id","tvdbId","name","abbreviation"}, 
				"tvdbid=?", 
				new String[]{tvdbId}, 
				null, null, null);
		Log.d(TAG,new StringBuilder("findById: ")
			.append(tvdbId)
			.toString());
		ArrayList<Language> languages = convertCursorToLanguage(cursor);
		return languages.get(0);
	}

	public Language findByAbbreviation(String abbreviation) {
		Cursor cursor = query(mDatabase, 
				mLanguageTable, 
				new String[]{"_id","tvdbId","name","abbreviation"}, 
				"abbreviation=?", 
				new String[]{abbreviation}, 
				null, null, null);
		Log.d(TAG,new StringBuilder("findByAbbreviation: ")
			.append(abbreviation)
			.toString());
		ArrayList<Language> languages = convertCursorToLanguage(cursor);
		return languages.get(0);
	}

	public ArrayList<Language> findAll() {
		Cursor cursor = query(mDatabase, 
				mLanguageTable, 
				new String[]{"_id","tvdbId","name","abbreviation"}, 
				null, null, null, null, null);
		return convertCursorToLanguage(cursor);
	}
	
	public ArrayList<Language> convertCursorToLanguage(Cursor cursor) {
		ArrayList<Language> languages = new ArrayList<Language>();
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Language language = new Language();
				language.id = cursor.getString(cursor.getColumnIndex("tvdbId"));
				language.name = cursor.getString(cursor.getColumnIndex("name"));
				language.abbreviation = cursor.getString(cursor.getColumnIndex("abbreviation"));
				languages.add(language);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		return languages;
	}
}
