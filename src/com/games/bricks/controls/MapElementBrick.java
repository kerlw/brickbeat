package com.games.bricks.controls;

import android.graphics.Canvas;

import com.games.bricks.common.BrickData;

public class MapElementBrick extends MapElement {
//	public interface MapElementBrickCreator {
//		public MapElementBrick createBrickFromBrickData(BrickData data);
//	}
//	

	public static MapElementCreator<MapElementBrick, BrickData> CREATOR 
	= new MapElementCreator<MapElementBrick, BrickData>() {
		public MapElementBrick createElementFromData(BrickData data) {
			return new MapElementBrick(data);
		}
	};
	
	public BrickData mBrickData;
	
	private MapElementBrick(BrickData data) {
		mBrickData = data;	
		this.setBound(data.mPosX*10, data.mPosY*10, 20, 10);
	}	
	@Override
	public void doDraw(Canvas canvas) {
		// !!!!CAUTION!!!!
		// DO NOTHING, this element would be drawn in MapLayerBrick
	}

}
