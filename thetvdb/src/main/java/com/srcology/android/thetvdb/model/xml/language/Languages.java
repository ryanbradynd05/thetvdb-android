package com.srcology.android.thetvdb.model.xml.language;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Languages {
	@ElementList(inline=true, entry="Language", required=false)
	public ArrayList<Language> languages = new ArrayList<Language>();
}
