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
package com.srcology.android.thetvdb.model.xml.search;

import org.simpleframework.xml.Element;

public class Series {
	@Element(required=false)
	public String seriesid;
	@Element(required=false)
	public String language;
	@Element(required=false)
	public String SeriesName;
	@Element(required=false)
	public String banner;
	@Element(required=false)
	public String Overview;
	@Element(required=false)
	public String FirstAired;
	@Element(required=false)
	public String IMDB_ID;
	@Element(required=false)
	public String id;
}
