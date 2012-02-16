package com.srcology.android.thetvdb.model.xml.newtoday;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Rss {
	@Element(required=false)
	public String date;
	@Element(name="channel", required=false)
	public Channel channel;
}
