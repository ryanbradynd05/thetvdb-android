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
