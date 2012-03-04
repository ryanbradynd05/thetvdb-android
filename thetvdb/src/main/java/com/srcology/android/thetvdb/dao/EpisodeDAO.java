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
import com.srcology.android.thetvdb.model.xml.show.Episode;

public class EpisodeDAO extends TvdbDAO {
	private static final String TAG = TvdbApp.TAG;
	private Context mContext;
	public static SQLiteDatabase mDatabase;
	private String mEpisodeTable;	

	public EpisodeDAO(Context context) {
		mContext = context;
		String dbName = mContext.getString(R.string.db_name);
		mEpisodeTable = mContext.getString(R.string.episode_table);
		Log.d(TAG, new StringBuilder("Open or create database: ")
			.append(dbName)
			.toString());
		mDatabase = mContext.openOrCreateDatabase(dbName, 
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		createTable();
	}

	public void createTable() {
		String createLanguage = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
			.append(mEpisodeTable)
			.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ")
			.append("tvdbId INTEGER, combinedEpisodeNumber TEXT, combinedSeason TEXT, dvdChapter TEXT, dvdDiscId TEXT, dvdEpisodeNumber TEXT, ")
			.append("dvdSeason TEXT, director TEXT, epImgFlag TEXT, episodeName TEXT, episodeNumber INTEGER, firstAired TEXT, guestStars TEXT, ")
			.append("imdbId TEXT, language TEXT, overview TEXT, productionCode TEXT, rating TEXT, ratingCount TEXT, seasonNumber INTEGER, ")
			.append("writer TEXT, absoluteNumber TEXT, airsAfterSeason TEXT, airsBeforeEpisode TEXT, airsBeforeSeason TEXT, filename TEXT, ")
			.append("lastUpdated TEXT, seasonId INTEGER, seriesId INTEGER);")
			.toString();
		createTable(mDatabase, 
				createLanguage);
	}

	public void delete(String episodeId) {
		delete(mDatabase, 
				mEpisodeTable, 
				"tvdbId=?", 
				new String[]{episodeId});
	}

	public void deleteBySeries(String seriesId) {
		delete(mDatabase, 
				mEpisodeTable, 
				"seriesId=?", 
				new String[]{seriesId});
	}

	public void deleteAll() {
		delete(mDatabase, 
				mEpisodeTable, 
				null, 
				null);
	}

	public void insert(Episode episode) {
		ContentValues values = convertEpisodeToValues(episode);
		insert(mDatabase, 
				mEpisodeTable,
				"", 
				values);
	}

	public void insertMultipleEpisodes(ArrayList<Episode> episodes) {
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();
		for (Episode episode: episodes) {
			ContentValues currentValues = convertEpisodeToValues(episode);
			values.add(currentValues);
		}
		insertMultiple(mDatabase, 
				mEpisodeTable,
				"", 
				values);
	}

	public ContentValues convertEpisodeToValues(Episode episode) {
		String tvdbId = episode.id;
		String combinedEpisodeNumber = episode.Combined_episodenumber;
		String combinedSeason = episode.Combined_season;
		String dvdChapter = episode.DVD_chapter;
		String dvdDiscId = episode.DVD_discid;
		String dvdEpisodeNumber = episode.DVD_episodenumber;
		String dvdSeason = episode.DVD_season;
		String director = episode.Director;
		String epImgFlag = episode.EpImgFlag;
		String episodeName = episode.EpisodeName;
		String episodeNumber = episode.EpisodeNumber;
		String firstAired = episode.FirstAired;
		String guestStars = episode.GuestStars;
		String imdbId = episode.IMDB_ID;
		String language = episode.Language;
		String overview = episode.Overview;
		String productionCode = episode.ProductionCode;
		String rating = episode.Rating;
		String ratingCount = episode.RatingCount;
		String seasonNumber = episode.SeasonNumber;
		String writer = episode.Writer;
		String absoluteNumber = episode.absolute_number;
		String airsAfterSeason = episode.airsafter_season;
		String airsBeforeEpisode = episode.airsbefore_episode;
		String airsBeforeSeason = episode.airsbefore_season;
		String filename = episode.filename;
		String lastUpdated = episode.lastupdated;
		String seasonId = episode.seasonid;
		String seriesId = episode.seriesid;
		ContentValues values = new ContentValues();
		values.put("tvdbId", tvdbId);
		values.put("combinedEpisodeNumber", combinedEpisodeNumber);
		values.put("combinedSeason", combinedSeason);
		values.put("dvdChapter", dvdChapter);
		values.put("dvdDiscId", dvdDiscId);
		values.put("dvdEpisodeNumber", dvdEpisodeNumber);
		values.put("dvdSeason", dvdSeason);
		values.put("director", director);
		values.put("epImgFlag", epImgFlag);
		values.put("episodeName", episodeName);
		values.put("episodeNumber", episodeNumber);
		values.put("firstAired", firstAired);
		values.put("guestStars", guestStars);
		values.put("imdbId", imdbId);
		values.put("language", language);
		values.put("overview", overview);
		values.put("productionCode", productionCode);
		values.put("rating", rating);
		values.put("ratingCount", ratingCount);
		values.put("seasonNumber", seasonNumber);
		values.put("writer", writer);
		values.put("absoluteNumber", absoluteNumber);
		values.put("airsAfterSeason", airsAfterSeason);
		values.put("airsBeforeEpisode", airsBeforeEpisode);
		values.put("airsBeforeSeason", airsBeforeSeason);
		values.put("filename", filename);
		values.put("lastUpdated", lastUpdated);
		values.put("seasonId", seasonId);
		values.put("seriesId", seriesId);
		return values;
	}

	public Episode findByEpisodeId(String episodeId) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"tvdbId=?", 
				new String[]{episodeId}, 
				null, null, 
				"episodeNumber");
		ArrayList<Episode> seasons = convertCursorToEpisode(cursor);
		return seasons.get(0);
	}

	public ArrayList<Episode> findBySeasonId(String seasonId) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"seasonId=?", 
				new String[]{seasonId}, 
				null, null, 
				"episodeNumber");
		ArrayList<Episode> seasons = convertCursorToEpisode(cursor);
		return seasons;
	}

	public Cursor findCursorBySeasonId(String seasonId) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"seasonId=?", 
				new String[]{seasonId}, 
				null, null, 
				"episodeNumber");
		return cursor;
	}

	public Cursor findCursorBySeriesId(String seriesId) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"seriesId=?", 
				new String[]{seriesId}, 
				null, null, 
				"FirstAired");
		return cursor;
	}

	public ArrayList<Episode> findBySeriesId(String seriesId) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"seriesId=?", 
				new String[]{seriesId}, 
				null, null, 
				"FirstAired");
		ArrayList<Episode> episodes = convertCursorToEpisode(cursor);
		return episodes;
	}

	public ArrayList<Episode> findBySeriesAndSeason(String seriesId, String seasonNumber) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"seriesId=? AND seasonNumber=?", 
				new String[]{seriesId, seasonNumber}, 
				null, null, 
				"episodeNumber");
		ArrayList<Episode> episodes = convertCursorToEpisode(cursor);
		return episodes;
	}

	public Cursor findCursorBySeriesAndSeason(String seriesId, String seasonNumber) {
		Cursor cursor = query(mDatabase, 
				mEpisodeTable, 
				new String[]{"_id","tvdbId","combinedEpisodeNumber","combinedSeason","dvdChapter","dvdDiscId","dvdEpisodeNumber",
				"dvdSeason","director","epImgFlag","episodeName","episodeNumber","firstAired","guestStars","imdbId","language",
				"overview","productionCode","rating","ratingCount","seasonNumber","writer","absoluteNumber","airsAfterSeason",
				"airsBeforeEpisode","airsBeforeSeason","filename","lastUpdated","seasonId","seriesId"}, 
				"seriesId=? AND seasonNumber=?", 
				new String[]{seriesId, seasonNumber}, 
				null, null, 
				"episodeNumber");
		return cursor;
	}
	
	private ArrayList<Episode> convertCursorToEpisode(Cursor cursor) {
		ArrayList<Episode> episodes = new ArrayList<Episode>();
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				Episode episode = new Episode();
				episode.id = cursor.getString(cursor.getColumnIndex("tvdbId"));
				episode.Combined_episodenumber = cursor.getString(cursor.getColumnIndex("combinedEpisodeNumber"));
				episode.Combined_season = cursor.getString(cursor.getColumnIndex("combinedSeason"));
				episode.DVD_chapter = cursor.getString(cursor.getColumnIndex("dvdChapter"));
				episode.DVD_discid = cursor.getString(cursor.getColumnIndex("dvdDiscId"));
				episode.DVD_episodenumber = cursor.getString(cursor.getColumnIndex("dvdEpisodeNumber"));
				episode.DVD_season = cursor.getString(cursor.getColumnIndex("dvdSeason"));
				episode.Director = cursor.getString(cursor.getColumnIndex("director"));
				episode.EpImgFlag = cursor.getString(cursor.getColumnIndex("epImgFlag"));
				episode.EpisodeName = cursor.getString(cursor.getColumnIndex("episodeName"));
				episode.EpisodeNumber = cursor.getString(cursor.getColumnIndex("episodeNumber"));
				episode.FirstAired = cursor.getString(cursor.getColumnIndex("firstAired"));
				episode.GuestStars = cursor.getString(cursor.getColumnIndex("guestStars"));
				episode.IMDB_ID = cursor.getString(cursor.getColumnIndex("imdbId"));
				episode.Language = cursor.getString(cursor.getColumnIndex("language"));
				episode.Overview = cursor.getString(cursor.getColumnIndex("overview"));
				episode.ProductionCode = cursor.getString(cursor.getColumnIndex("productionCode"));
				episode.Rating = cursor.getString(cursor.getColumnIndex("rating"));
				episode.RatingCount = cursor.getString(cursor.getColumnIndex("ratingCount"));
				episode.SeasonNumber = cursor.getString(cursor.getColumnIndex("seasonNumber"));
				episode.Writer = cursor.getString(cursor.getColumnIndex("writer"));
				episode.absolute_number = cursor.getString(cursor.getColumnIndex("absoluteNumber"));
				episode.airsafter_season = cursor.getString(cursor.getColumnIndex("airsAfterSeason"));
				episode.airsbefore_episode = cursor.getString(cursor.getColumnIndex("airsBeforeEpisode"));
				episode.airsbefore_season = cursor.getString(cursor.getColumnIndex("airsBeforeSeason"));
				episode.filename = cursor.getString(cursor.getColumnIndex("filename"));
				episode.lastupdated = cursor.getString(cursor.getColumnIndex("lastUpdated"));
				episode.seasonid = cursor.getString(cursor.getColumnIndex("seasonId"));
				episode.seriesid = cursor.getString(cursor.getColumnIndex("seriesId"));
				episodes.add(episode);
				cursor.moveToNext();
			} while (!cursor.isAfterLast());
		}
		return episodes;
	}
}
