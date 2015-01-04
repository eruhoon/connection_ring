package com.billy.connectionring;

import java.util.HashMap;
import java.util.List;

import com.billy.connectionring.model.Component;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class SlidingListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<SlidingChildModel>> _listDataChild;

	public SlidingListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<SlidingChildModel>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		
	}
	
	public void selectDashboard(SlidingChildModel child)
	{
		if(child.getType() == Constants.ITEM_MEMOGROUP)
		{
			
		}
	}

	public void selectBookmark(SlidingChildModel child)
	{
		// to do
		// book���� �������� �޸� ����Ʈ�並 �����ϴ� �ڵ带 �ۼ��Ѵ�.
	}
	/*
	public void AddDashBoard(int type, String name)
	{
		this._listDataChild.get("DashBoard").add(new SlidingChildModel(type, name));
		this.notifyDataSetChanged();
	}
	
	public void removeDashBoard(int type, String name)
	{
		List<SlidingChildModel> child = this._listDataChild.get("DashBoard");
		for(int i = 0; i < child.size(); i++) {
			if(child.get(i)._type == (type) && child.get(i)._name.equals(name))
				child.remove(i);
		}
		this.notifyDataSetChanged();
	}
	
	public void AddBookmark(int type, String name)
	{
		this._listDataChild.get("Bookmark").add(new SlidingChildModel(type, name));
		this.notifyDataSetChanged();
	}
	
	public void removeBookmark(int type, String name)
	{
		List<SlidingChildModel> child = this._listDataChild.get("Bookmark");
		for(int i = 0; i < child.size(); i++) {
			if(child.get(i)._type == (type) && child.get(i)._name.equals(name))
				child.remove(i);
		}
		this.notifyDataSetChanged();
	}
	*/
	
	@Override
	public SlidingChildModel getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final SlidingChildModel child = getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.slide_list_child, null);
		}
		
		// icon
	    ImageView image = (ImageView)convertView.findViewById(R.id.imv_slide_child);
	    int imageResourceId = 0;
		int type = child.getType();
		
		String text = null;
		if(type == Constants.ITEM_MEMO) {
			imageResourceId = R.drawable.slide_child_memo;
			text = child.getTitle();
		}
		else if(type == Constants.ITEM_MEMOGROUP) {
			imageResourceId = R.drawable.slide_child_memogroup;
			text = child.getDashname();
		}
		
		image.setImageResource(imageResourceId);
		
		// content
		TextView txtView = (TextView) convertView
				.findViewById(R.id.txt_slide_child);
		txtView.setText(text);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		try {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
		}catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.slide_list_parent, null);
		}
		
		// icon
		ImageView image = (ImageView)convertView.findViewById(R.id.imv_slide_parent);
		int imageResourceId = 0;
		if(groupPosition == Constants.SLIDE_ID) imageResourceId = R.drawable.slide_people;
		if(groupPosition == Constants.SLIDE_ALLNOTE) imageResourceId = R.drawable.slide_allnote;
		if(groupPosition == Constants.SLIDE_DASHBOARD) imageResourceId = R.drawable.slide_dashboard;
		if(groupPosition == Constants.SLIDE_BOOKMARK) imageResourceId = R.drawable.slide_bookmark;
		if(groupPosition == Constants.SLIDE_SETTINGS) imageResourceId = R.drawable.slide_settings;
		image.setImageResource(imageResourceId);
		
		// text
		String headerTitle = (String) getGroup(groupPosition);

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.txt_slide_parent);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

				
		// indicator
		image = (ImageView)convertView.findViewById(R.id.imv_slide_indicator);
		if(groupPosition == 2 || groupPosition == 3){
			imageResourceId = isExpanded ? R.drawable.slide_indicator_up : R.drawable.slide_indicator_down;
			image.setImageResource(imageResourceId);
			
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.INVISIBLE);
		}
		
		
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void update(List<String> _listDataHeader, HashMap<String, List<SlidingChildModel>> _listDataChild)
	{
		this._listDataHeader = _listDataHeader;
		this._listDataChild = _listDataChild;
		this.notifyDataSetChanged();
	}

}
