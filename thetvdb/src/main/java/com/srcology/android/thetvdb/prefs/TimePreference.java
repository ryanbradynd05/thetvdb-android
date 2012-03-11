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
package com.srcology.android.thetvdb.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.srcology.android.thetvdb.R;

/**
 * Custom preference for time selection. Hour and minute are persistent and
 * stored separately as ints in the underlying shared preferences under keys
 * KEY.hour and KEY.minute, where KEY is the preference's key.
 * from http://www.twodee.org/blog/?p=1037
 */
public class TimePreference extends DialogPreference {

  /** The widget for picking a time */
  private TimePicker timePicker;

  /** Default hour */
  private static final int DEFAULT_HOUR = 8;

  /** Default minute */
  private static final int DEFAULT_MINUTE = 0;

  /**
   * Creates a preference for choosing a time based on its XML declaration.
   *
   * @param context
   * @param attributes
   */
  public TimePreference(Context context,
                        AttributeSet attributes) {
    super(context, attributes);
    setPersistent(false);
  }

  /**
   * Initialize time picker to currently stored time preferences.
   *
   * @param view
   * The dialog preference's host view
   */
  @Override
  public void onBindDialogView(View view) {
    super.onBindDialogView(view);
    timePicker = (TimePicker) view.findViewById(R.id.prefTimePicker);
    timePicker.setCurrentHour(getSharedPreferences().getInt(getKey() + ".hour", DEFAULT_HOUR));
    timePicker.setCurrentMinute(getSharedPreferences().getInt(getKey() + ".minute", DEFAULT_MINUTE));
    timePicker.setIs24HourView(DateFormat.is24HourFormat(timePicker.getContext()));
  }

  /**
   * Handles closing of dialog. If user intended to save the settings, selected
   * hour and minute are stored in the preferences with keys KEY.hour and
   * KEY.minute, where KEY is the preference's KEY.
   *
   * @param okToSave
   * True if user wanted to save settings, false otherwise
   */
  @Override
  protected void onDialogClosed(boolean okToSave) {
    super.onDialogClosed(okToSave);
    if (okToSave) {
      timePicker.clearFocus();
      SharedPreferences.Editor editor = getEditor();
      editor.putInt(getKey() + ".hour", timePicker.getCurrentHour());
      editor.putInt(getKey() + ".minute", timePicker.getCurrentMinute());
      editor.commit();
    }
  }
}