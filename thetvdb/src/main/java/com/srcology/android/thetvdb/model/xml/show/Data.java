package com.srcology.android.thetvdb.model.xml.show;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Data {
	@Element(name="Series", required=false)
	public Series series = new Series();
	@ElementList(inline=true, entry="Episode", required=false)
	public ArrayList<Episode> episodes = new ArrayList<Episode>();
}
