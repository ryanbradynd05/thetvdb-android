package com.srcology.android.thetvdb.model.xml.search;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Data {
	@ElementList(inline=true, entry="Series", required=false)
	public ArrayList<Series> series = new ArrayList<Series>();

}
