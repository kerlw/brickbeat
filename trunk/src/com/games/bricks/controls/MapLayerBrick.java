package com.games.bricks.controls;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.games.bricks.common.BrickData;
import com.games.bricks.common.MapData;

public class MapLayerBrick extends MapElement {
	
	//
	// TODO 把砖块对象按每行来保存列表，地图中使用砖块行对象来检测碰撞以提高效率
	//
	//private final int ROW_NUMBERS = 30;
	//地图上砖块区的一行
	private class BricksRow extends MapElement {
		
		@Override
		public boolean isCollided(MapElement mapElement) {
			// TODO Auto-generated method stub
			return super.isCollided(mapElement);
		}
	}
	//-------------TO DO-----------------------------------------------------
	
	private HashMap<Integer, Integer> typeIndexMap;
	
	private MapData mMapData;
	
	public MapLayerBrick() {
		typeIndexMap = new HashMap<Integer, Integer>();
	}
	
	public void addBrickType(Drawable dr, int type) {
		mImages.add(dr);
		int location = mImages.size()-1;
		typeIndexMap.put(type, location);
	}
	
	public void initBricks(MapData data) {
		mMapData = data;
		ArrayList<BrickData> bricks = data.mBricks;
		int size = bricks.size();
		for(int i=0;i<size;++i) {
			BrickData bd = bricks.get(i);
			MapElementBrick eleBrick = MapElementBrick.CREATOR.createElementFromData(bd);
//			eleBrick.setBound(bd.mPosX*10, bd.mPosY*10, 20, 10);
			this.addSubElement(eleBrick);
		}
	}
	
	public MapLayerBrick(Drawable dr, int left, int top, int width, int height) {
		mImages.clear();
		mImages.add(dr);
		mBound.left = left;		mBound.right = left + width;
		mBound.top = top;		mBound.bottom = top + height;
	}

	@Override
	public void doDraw(Canvas canvas) {
		int size = mComponents.size();
		for(int index = 0; index < size; ++index) {
			MapElementBrick brick = (MapElementBrick)mComponents.get(index);
			if ( !typeIndexMap.containsKey(brick.mBrickData.mType) ) 
				continue;
			
			int imgIndex = typeIndexMap.get(brick.mBrickData.mType); 
			Drawable dr = mImages.get(imgIndex);
			Rect bound = new Rect(brick.mBound);
			bound.offset(mBound.left, mBound.top);
			dr.setBounds(bound);
			dr.draw(canvas);
		}
		
	}
	
	public Rect addBrickAt(Point pt, int type) {
		BrickData bd = new BrickData();
		bd.mType = type;
		bd.mPosX = (pt.x - mBound.left) / 10;
		bd.mPosY = (pt.y - mBound.top) / 10;
		MapElementBrick brick = MapElementBrick.CREATOR.createElementFromData(bd);
		boolean couldDoAdd = true;
		int size = mComponents.size();
		for(int i=0; i<size; ++i) {
			MapElement ele = mComponents.get(i);
			if( ele.getClass().equals(MapElementBrick.class) && ele.isCollided(brick) ) {
				couldDoAdd = false;
			}
		}
		if(couldDoAdd)  {
			this.addSubElement(brick);
			mMapData.mBricks.add(bd);
			Rect rect = new Rect(brick.mBound);
			rect.offset(mBound.left, mBound.top);
			return rect;
		} else
			return null;
	}
	
	public Rect removeBrickAt(Point point) {
		Point pt = new Point(point.x-mBound.left, point.y-mBound.top);
		int size = mComponents.size();
		for(int i=0; i<size; ++i) {
			MapElement ele = mComponents.get(i);
			if( ele.getClass().equals(MapElementBrick.class) && ele.isPointInBound(pt) ) {
				mComponents.remove(ele);
				Rect rect = new Rect(ele.mBound);
				rect.offset(mBound.left, mBound.top);
				BrickData bd = ((MapElementBrick)ele).mBrickData;
				mMapData.mBricks.remove(bd);
				bd = null;
				ele = null;
				return rect;
			}
		}
		return null;
	}
	
}
