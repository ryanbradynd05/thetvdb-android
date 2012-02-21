package com.srcology.android.thetvdb.model.xml.updates;

import org.simpleframework.xml.Element;

public class Episode {
	@Element(required=false)
	public String id;
	@Element(required=false)
	public String time;
	@Element(required=false)
	public String Series;
}
