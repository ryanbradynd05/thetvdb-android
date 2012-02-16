package com.srcology.android.thetvdb.model.xml.language;

import org.simpleframework.xml.Element;

@Element(name="Language", required=false)
public class Language {
	@Element(required=false)
	public String name;
	
	@Element(required=false)
	public String abbreviation;
	
	@Element(required=false)
	public String id;
}
