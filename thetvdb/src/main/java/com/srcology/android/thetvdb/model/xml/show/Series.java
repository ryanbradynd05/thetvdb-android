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

public class Series {
	@Element(required=false)
	public String id;
	@Element(required=false)
	public String Actors;
	@Element(required=false)
	public String Airs_DayOfWeek;
	@Element(required=false)
	public String Airs_Time;
	@Element(required=false)
	public String ContentRating;
	@Element(required=false)
	public String FirstAired;
	@Element(required=false)
	public String Genre;
	@Element(required=false)
	public String IMDB_ID;
	@Element(required=false)
	public String Language;
	@Element(required=false)
	public String Network;
	@Element(required=false)
	public String NetworkID;
	@Element(required=false)
	public String Overview;
	@Element(required=false)
	public String Rating;
	@Element(required=false)
	public String RatingCount;
	@Element(required=false)
	public String Runtime;
	@Element(required=false)
	public String SeriesID;
	@Element(required=false)
	public String SeriesName;
	@Element(required=false)
	public String Status;
	@Element(required=false)
	public String added;
	@Element(required=false)
	public String addedBy;
	@Element(required=false)
	public String banner;
	@Element(required=false)
	public String fanart;
	@Element(required=false)
	public String lastupdated;
	@Element(required=false)
	public String poster;
	@Element(required=false)
	public String zap2it_id;
}
