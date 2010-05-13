package com.games.bricks.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.games.bricks.R;
import com.games.bricks.common.MapDataProvider;
import com.games.bricks.view.BricksView;

public class Bricks extends Activity  implements OnClickListener {
	
//	private BricksThread mThread;
	private BricksView mBricksView;
	
	private Button mBtnNewGame;
	private Button mBtnEditMap;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
        MapDataProvider mdp = new MapDataProvider(this);       
        setContentView(R.layout.main_menu);
        mBtnEditMap = (Button) findViewById(R.id.edit_map);
        mBtnEditMap.setOnClickListener(this);
        mBtnNewGame = (Button) findViewById(R.id.new_game);
        mBtnNewGame.setOnClickListener(this);
        
        checkMapFiles();
        
        Rect rc1 = new Rect(0,0,5,5);
        Rect rc2 = new Rect(5,5,10,10);
        rc1.intersect(rc2);
        
        
    }

    // 检测地图文件是否存在，如果不存在，则将/res/raw下的初始化地图copy到数据文件目录
    protected void checkMapFiles() {
    	String filePath = this.getFilesDir().getPath();
    	File file = new File(filePath, MapDataProvider.MAP_LIST_FILE_NAME);
    	try {
			if(!file.createNewFile()) {
				return;
			}
			
			Resources res = getResources();
			byte[] buffer = new byte[1024]; 
			int length = 0;
			FileOutputStream out;
			
			InputStream input = res.openRawResource(R.raw.maplist);
			length = input.read(buffer);
			input.close();
			out = openFileOutput(MapDataProvider.MAP_LIST_FILE_NAME, MODE_PRIVATE);
			out.write(buffer, 0, length);
			out.flush();
			out.close();
			
			String fileName = MapDataProvider.MAP_LEVEL_FILE_NAME_PREFIX + "01";
			input = res.openRawResource(R.raw.level01);
			length = input.read(buffer);
			input.close();
			out = openFileOutput(fileName, MODE_PRIVATE);
			out.write(buffer, 0, length);
			out.flush();
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
    	
    	
    }
    
	@Override
	protected void onPause() {
		super.onPause();
//		mThread.pause();
		
	}

	@Override
	public final void onClick(View v) {
		if(v == mBtnNewGame) {
			Intent it = new Intent(this, GameScence.class);
//			it.putExtra(GameScence.KEY_GAME_MODE, GameScence.GAME_PLAY_MODE);
			startActivity(it);
		}
		else if(v == mBtnEditMap) {
			Intent it = new Intent(this, MapList.class);
//			it.putExtra(GameScence.KEY_GAME_MODE, GameScence.MAP_EDIT_MODE);
			startActivity(it);
		}
	}
	
	
    
    
}