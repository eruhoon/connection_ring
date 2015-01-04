package com.billy.connectionring.search;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;

public class SearchListAdapter extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private List<SearchListModel> searchItems;
	
	public SearchListAdapter(Activity activity, List<SearchListModel> searchItems) {
		this.activity = activity;
		this.searchItems = searchItems;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.searchItems.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return this.searchItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (inflater == null) 
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.search_list_item, null);
		
		// icon
	    ImageView image = (ImageView)convertView.findViewById(R.id.imv_searchitem);
	    int imageResourceId = 0;
		int type = searchItems.get(position)._type;
		
		if(type == Constants.ITEM_MEMO) imageResourceId = R.drawable.slide_child_memo;
		if(type == Constants.ITEM_MEMOGROUP) imageResourceId = R.drawable.slide_child_memogroup;
		image.setImageResource(imageResourceId);
		
		//text
		
		TextView tv_name = (TextView) convertView.findViewById(R.id.txt_searchitem);
		tv_name.setText(searchItems.get(position)._name);
		
		
		return convertView;
	}

}
