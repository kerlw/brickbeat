package com.games.bricks.controls;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.games.bricks.common.BallData;
import com.games.bricks.common.MoveableElement;

public class MapElementBall extends MapElement implements MoveableElement {

	public static MapElementCreator<MapElementBall, BallData> CREATOR 
	= new MapElementCreator<MapElementBall, BallData>() {
		public MapElementBall createElementFromData(BallData data) {
			return new MapElementBall(data);
		}
	};
	
	private final int SIZE_SMALL = 1;
	private final int SIZE_NORMAL = 2;
	private final int SIZE_BIG = 3;
	
	public BallData mBallData;
	
	public MapElementBall(BallData data) {
		mBallData = data;
	}
	
	

	public void beBigger() {
		BallData dt = mBallData;
		if(dt.mSize<SIZE_BIG)
			dt.mSize++;
	}
	
	public void beSmaller() {
		BallData dt = mBallData;
		if(dt.mSize>SIZE_SMALL)
			dt.mSize--;
	}
	
	public void speedUp() {
		BallData dt = mBallData;
		dt.mSpeed += 1;
		
	}
	
	public void speedDown() {
		BallData dt = mBallData;
		if(dt.mSpeed>1)
			dt.mSpeed -= 1;
	}
	@Override
	public void calcNewPosition() {
		BallData dt = mBallData;
		float newX = dt.mPosXf + (float)(dt.mSpeed*Math.cos(dt.mAngle));
		float newY = dt.mPosYf + (float)(dt.mSpeed*Math.sin(dt.mAngle));
		
		int radius = 2 + dt.mSize; 
		
		dt.mPosXf = newX;
		dt.mPosYf = newY;
		dt.mPosX = (int)newX;
		dt.mPosY = (int)newY;
		
		if(newX-radius<0) {
			dt.mPosX = radius;
			dt.mPosXf = dt.mPosX;
			dt.mAngle = dt.mAngle > Math.PI ?
				(float)(Math.PI*3 - dt.mAngle) : (float)(Math.PI - dt.mAngle);
		}
		
		if(newY-radius<0) {
			dt.mPosY = radius;
			dt.mPosYf = dt.mPosY;
			dt.mAngle = (float)(Math.PI*2 - dt.mAngle);
		}
		
		if(newX+radius>300) {
			dt.mPosX = 300-radius;
			dt.mPosXf = dt.mPosX;
			dt.mAngle =  dt.mAngle > Math.PI ?
					(float)(Math.PI*3 - dt.mAngle) : (float)(Math.PI - dt.mAngle);
		}
		
		if(newY+radius>400) {
			dt.mPosY = 400-radius;
			dt.mPosYf = dt.mPosY;
			dt.mAngle = (float)(Math.PI*2 - dt.mAngle);
		}
	}



	@Override
	public void doDraw(Canvas canvas) {
		if(mImageIndex >= mImages.size())
			return;
		
		Drawable dr = mImages.get(mImageIndex);
		BallData dt = mBallData;
		int radius = 2 + dt.mSize;
		int offx = mParent.mBound.left + mParent.childOffX;
		int offy = mParent.mBound.top + mParent.childOffY;
		Rect rc = new Rect(dt.mPosX-radius+offx, dt.mPosY-radius+offy, dt.mPosX+radius+offx, dt.mPosY+radius+offy);
		dr.setBounds(rc);
		
		dr.draw(canvas);
	}
	
	
}
