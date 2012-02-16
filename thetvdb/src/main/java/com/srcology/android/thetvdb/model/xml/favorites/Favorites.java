package com.srcology.android.thetvdb.model.xml.favorites;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Favorites {
	@ElementList(inline=true, entry="Series", required=false)
	public ArrayList<String> series = new ArrayList<String>();
	public ArrayList<String> seriesNames = new ArrayList<String>();
}
