package com.srcology.android.thetvdb.model.xml.newtoday;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="channel", required=false)
public class Channel {
	@Element(required=false)
	public String title;

	@Element(required=false)
	public String ttl;

	@Element(required=false)
	public String link;

	@Element(required=false)
	public String description;

	@Element(required=false)
	public String language;

	@Element(required=false)
	public String pubDate;
	
	@ElementList(inline=true, entry="item", required=false)
	public ArrayList<Item> items = new ArrayList<Item>();

}
