package com.srcology.android.thetvdb.model.xml.updates;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Data {
	@ElementList(inline=true, name="Series", required=false)
	public ArrayList<Series> series = new ArrayList<Series>();
	@ElementList(inline=true, entry="Episode", required=false)
	public ArrayList<Episode> episodes = new ArrayList<Episode>();
	@ElementList(inline=true, entry="Banner", required=false)
	public ArrayList<Banner> banners = new ArrayList<Banner>();
}
