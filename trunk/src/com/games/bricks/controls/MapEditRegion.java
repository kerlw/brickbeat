package com.games.bricks.controls;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.games.bricks.common.BrickData;
import com.games.bricks.common.MapData;

public class MapEditRegion extends MapElement {

	public MapEditRegion() {
		childOffX = 1;
		childOffY = 1;
	}
	
	@Override
	public void doDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		canvas.drawRect(mBound, paint);
		super.doDraw(canvas);
	}
	
	@Override
	public boolean isCollided(MapElement ele) {
		int size = mComponents.size();
		for(int i=0; i<size; ++i) {
			if(mComponents.get(i).isCollided(ele))
				return true;
		}
		return false;
	}
	
	public void addBrickIfThereIsNone(MapLayerBrick brick) {
		if(!isCollided(brick))
			mComponents.add(brick);
	}
	
}
