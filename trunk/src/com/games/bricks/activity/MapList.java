package com.games.bricks.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.games.bricks.R;
import com.games.bricks.common.MapData;
import com.games.bricks.common.MapDataProvider;

public class MapList extends ListActivity {
	
	private MapDataProvider mMapDataProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.map_list);

		mMapDataProvider = new MapDataProvider(this);
		fillListData();
		
	}
	
	@Override
	protected void onResume() {
		fillListData();
		super.onResume();
	}

	private void fillListData() {
		mMapDataProvider.readMapData();
		ArrayAdapter<MapData> adapter = new ArrayAdapter<MapData>(
				this, android.R.layout.simple_list_item_1, mMapDataProvider.getMapDatas());
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent it = new Intent(this, MapEditor.class);
		MapData data = mMapDataProvider.getMapDatas().get((int)id);
		it.putExtra(MapEditor.KEY_ROW_ID, (int)id);
		it.putExtra(MapEditor.KEY_MAP_DATA, data);
		startActivity(it);
	}
	

}
