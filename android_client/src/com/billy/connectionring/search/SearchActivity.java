package com.billy.connectionring.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.connectionring.Constants;
import com.billy.connectionring.R;

public class SearchActivity extends Activity{
	
	ImageButton btn_search;
	TextView txt_search;
	ListView list_recentSearch;
	
	SearchListAdapter adapter;
	
	List<SearchListModel> searchItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		
		setting();
	}
	
	private void setting() {
		btn_search = (ImageButton)findViewById(R.id.btn_searchactivity_search);
		txt_search = (TextView)findViewById(R.id.edit_searchactivity_Search);
		list_recentSearch = (ListView)findViewById(R.id.list_recent_search);
		
		setList();
	}
	
	private void setList() {
		readData();
		
		adapter = new SearchListAdapter(this, searchItems);
		list_recentSearch.setAdapter(adapter);
		list_recentSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub

                Toast.makeText(
                        getApplicationContext(), 
                        "ITEM CLICK = " + position,
                        Toast.LENGTH_SHORT
                        ).show();
				
			}});
		
		adapter.notifyDataSetChanged();
	}
	
	private void readData() {
		searchItems = new ArrayList<SearchListModel>();
		
		//Todo -> db�κ��� read�ϱ�
		searchItems.add(new SearchListModel(Constants.ITEM_MEMO, "�˻��߾���1"));
		searchItems.add(new SearchListModel(Constants.ITEM_MEMO, "���� �˻��߾���1"));
		searchItems.add(new SearchListModel(Constants.ITEM_MEMOGROUP, "���� �׷��� �˻��߾���1"));
	}

}
