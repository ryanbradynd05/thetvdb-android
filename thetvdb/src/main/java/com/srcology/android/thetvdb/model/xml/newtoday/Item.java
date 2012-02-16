package com.srcology.android.thetvdb.model.xml.newtoday;

import org.simpleframework.xml.Element;

@Element(name="item", required=false)
public class Item {
	@Element(required=false)
	public String title;

	@Element(required=false)
	public String link;

	@Element(required=false)
	public String description;

	@Element(required=false)
	public String pubDate;
	
	public String showId;
	public String showName;
	public String episodeId;
	public String episodeName;
	public String seasonId;
}
