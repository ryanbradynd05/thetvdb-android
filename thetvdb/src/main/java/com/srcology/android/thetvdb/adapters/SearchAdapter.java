package com.srcology.android.thetvdb.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.srcology.android.thetvdb.R;
import com.srcology.android.thetvdb.TvdbApp;
import com.srcology.android.thetvdb.model.xml.language.Language;
import com.srcology.android.thetvdb.model.xml.search.Series;

public class SearchAdapter extends ArrayAdapter<Series> {
	private static final String TAG = TvdbApp.TAG;
    private ArrayList<Series> mItems;
    private Context mContext;

    public SearchAdapter(Context context, int textViewResourceId, ArrayList<Series> items) {
            super(context, textViewResourceId, items);
            mContext = context;
            mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Series series = mItems.get(position);
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.row, null);
    	}
        if (series != null) {
    		Log.d(TAG,"Add to list: " + series.SeriesName);
            TextView tt = (TextView) convertView.findViewById(R.id.toptext);
            TextView bt = (TextView) convertView.findViewById(R.id.bottomtext);
            if (tt != null) {
            	tt.setText(series.SeriesName);
            }
            if(bt != null){
				Language language = TvdbApp.languageDAO.findByAbbreviation(series.language);
				bt.setText(new StringBuilder(language.name)
					.append("   (")
					.append(series.seriesid)
					.append(")"));
            }
        }
        return convertView;
    }
}