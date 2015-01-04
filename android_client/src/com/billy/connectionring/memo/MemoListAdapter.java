package com.billy.connectionring.memo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.billy.connectionring.R;
import com.billy.connectionring.model.Component;


public class MemoListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Component>> _listDataChild;

	public MemoListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<Component>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Component getChild(int groupPosition, int childPosititon) {
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
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_memo_child, null);
		}
		
		final Component child = getChild(groupPosition, childPosition);
		
		String title = child.getTitle();
		String content = child.getContent();
		Date date = child.getRegdate();
		String hours = Integer.toString(date.getHours());
		String minutes = Integer.toString(date.getMinutes());
		
		// title
		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.lblListItemTitle);
		txtTitle.setText(title);
		
		// content
		TextView txtContent = (TextView) convertView
				.findViewById(R.id.lblListItemContent);
		
		// time
		String timeText;
		if(child.isAM(date)) timeText = "오전 " + hours + "시 : " + minutes + "분";
		else timeText = "오후" + hours + "시 : " + minutes + "분";
		

		txtContent.setText(timeText + " " + content);

		Spannable span = new SpannableString(txtContent.getText().toString());
		span.setSpan(new ForegroundColorSpan(Color.GREEN), 0, timeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txtContent.setText(span);
		
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(_listDataChild.get(this._listDataHeader.get(groupPosition)) == null)
			return 0;
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if(this._listDataHeader == null)
			return 0;
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
			convertView = infalInflater.inflate(R.layout.list_memo_parent, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		
		
		String headerTitle = (String) getGroup(groupPosition);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		
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

	public void update(List<String> _listDataHeader, HashMap<String, List<Component>> _listDataChild)
	{
		this._listDataHeader = _listDataHeader;
		this._listDataChild = _listDataChild;
		this.notifyDataSetChanged();
	}
}
