package com.games.bricks.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.games.bricks.view.GameScenceView;


public class GameScence extends Activity {
	
	private GameScenceView mMainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Bundle bl = getIntent().getExtras();
//		int level = (int)bl.getLong(GameScence.KEY_GAME_LEVEL);
//		ArrayList<MapData> mapDatas = bl.getParcelableArrayList(KEY_MAP_DATA);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mMainView = new GameScenceView(this, 0);
		setContentView(mMainView);
	}
	
	
	
}
