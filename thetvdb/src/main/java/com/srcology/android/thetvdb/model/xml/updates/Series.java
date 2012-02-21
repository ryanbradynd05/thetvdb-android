package com.srcology.android.thetvdb.model.xml.updates;

import org.simpleframework.xml.Element;

public class Series {
	@Element(required=false)
	public String id;
	@Element(required=false)
	public String time;
}
