/**
 * Copyright (C) 2012 Ryan Brady <rbrady@srcology.com>
 *
 * 		TheTVDB-Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.srcology.android.thetvdb.model.xml.newtoday;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="channel", required=false)
public class Channel {
	@Element(required=false)
	public String title;

	@Element(required=false)
	public String ttl;

	@Element(required=false)
	public String link;

	@Element(required=false)
	public String description;

	@Element(required=false)
	public String language;

	@Element(required=false)
	public String pubDate;
	
	@ElementList(inline=true, entry="item", required=false)
	public ArrayList<Item> items = new ArrayList<Item>();

}
