package com.billy.connectionring.quick;

import java.util.ArrayList;
import java.util.List;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;
import com.billy.connectionring.SlidingChildModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QuickPopupAdapter extends BaseAdapter{
	private Context m_Context = null;
	private List<SlidingChildModel> list_popup = null;
	
	public QuickPopupAdapter(Context context, List<SlidingChildModel> list_popup) {
		this.m_Context = context;
		this.list_popup = list_popup;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_popup.size();
	}
	@Override
	public SlidingChildModel getItem(int position) {
		// TODO Auto-generated method stub
		return list_popup.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.m_Context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.popup_row, null);
		}
		
		// icon
	    ImageView image = (ImageView)convertView.findViewById(R.id.iv_popup);
	    int imageResourceId = 0;
		
	    String text = null;
		if(list_popup.get(position).getType() == Constants.ITEM_MEMO) {
			imageResourceId = R.drawable.ic_memo;
			text = list_popup.get(position).getTitle();
		}else {
			imageResourceId = R.drawable.ic_memogroup;
			text = list_popup.get(position).getDashname();
		}
		
		image.setImageResource(imageResourceId);
		
		// content
		TextView txtView = (TextView) convertView
				.findViewById(R.id.tv_popup);
		txtView.setText(text);
		
		
		return convertView ;
	}
	
	public void update(List<SlidingChildModel> slideList) {
		this.list_popup = slideList;
		this.notifyDataSetChanged();
		
	}
}