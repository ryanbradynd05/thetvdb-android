package com.srcology.android.thetvdb.model.xml.updates;

import org.simpleframework.xml.Element;

public class Banner {
	@Element(required=false)
	public String Series;
	@Element(required=false)
	public String format;
	@Element(required=false)
	public String path;
	@Element(required=false)
	public String time;
	@Element(required=false)
	public String type;
}
