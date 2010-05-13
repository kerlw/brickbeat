package com.games.bricks.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.games.bricks.common.MapData;
import com.games.bricks.common.MapDataProvider;
import com.games.bricks.view.MapEditView;


public class MapEditor extends Activity {
	
	private MapEditView mMainView;
	private int mRowId;

	public final static String KEY_MAP_DATA = "MapData";
	public final static String KEY_ROW_ID	= "LevelNum";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MapData mapData = null;
		if (savedInstanceState != null) {
			mRowId = savedInstanceState.getInt(KEY_ROW_ID);
			mapData = savedInstanceState.getParcelable(KEY_MAP_DATA);
		}
		
		if (mapData == null) {
			Bundle bl = getIntent().getExtras();
			mRowId = bl.getInt(KEY_ROW_ID);
			mapData = bl.getParcelable(KEY_MAP_DATA);
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mMainView = new MapEditView(this, mapData);
		setContentView(mMainView);
		
	}

	@Override
	protected void onPause() {
		MapDataProvider mdp = new MapDataProvider(this);
		mdp.updateMapData(mMainView.getMapData(), mRowId);
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_ROW_ID, mRowId);
		outState.putParcelable(KEY_MAP_DATA, mMainView.getMapData());
	}
	
	
	
}
