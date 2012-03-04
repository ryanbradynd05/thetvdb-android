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
package com.srcology.android.thetvdb.model.xml.updates;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Data {
	@ElementList(inline=true, name="Series", required=false)
	public ArrayList<Series> series = new ArrayList<Series>();
	@ElementList(inline=true, entry="Episode", required=false)
	public ArrayList<Episode> episodes = new ArrayList<Episode>();
	@ElementList(inline=true, entry="Banner", required=false)
	public ArrayList<Banner> banners = new ArrayList<Banner>();
}
