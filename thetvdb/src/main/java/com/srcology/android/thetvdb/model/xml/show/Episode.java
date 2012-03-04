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
package com.srcology.android.thetvdb.model.xml.show;

import org.simpleframework.xml.Element;

public class Episode {
	@Element(required=false)
	public String id;
	@Element(required=false)
	public String Combined_episodenumber;
	@Element(required=false)
	public String Combined_season;
	@Element(required=false)
	public String DVD_chapter;
	@Element(required=false)
	public String DVD_discid;
	@Element(required=false)
	public String DVD_episodenumber;
	@Element(required=false)
	public String DVD_season;
	@Element(required=false)
	public String Director;
	@Element(required=false)
	public String EpImgFlag;
	@Element(required=false)
	public String EpisodeName;
	@Element(required=false)
	public String EpisodeNumber;
	@Element(required=false)
	public String FirstAired;
	@Element(required=false)
	public String GuestStars;
	@Element(required=false)
	public String IMDB_ID;
	@Element(required=false)
	public String Language;
	@Element(required=false)
	public String Overview;
	@Element(required=false)
	public String ProductionCode;
	@Element(required=false)
	public String Rating;
	@Element(required=false)
	public String RatingCount;
	@Element(required=false)
	public String SeasonNumber;
	@Element(required=false)
	public String Writer;
	@Element(required=false)
	public String absolute_number;
	@Element(required=false)
	public String airsafter_season;
	@Element(required=false)
	public String airsbefore_episode;
	@Element(required=false)
	public String airsbefore_season;	
	@Element(required=false)
	public String filename;
	@Element(required=false)
	public String lastupdated;
	@Element(required=false)
	public String seasonid;
	@Element(required=false)
	public String seriesid;

}
