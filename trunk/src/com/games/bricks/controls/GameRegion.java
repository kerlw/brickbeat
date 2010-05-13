package com.games.bricks.controls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.games.bricks.common.BallData;

public class GameRegion extends MapElement {
	
	public MapElementBall mBall;

	public GameRegion() {
		childOffX = 1;
		childOffY = 1;
		mBound.left = 9;
		mBound.top = 9;
		mBound.right = 311;
		mBound.bottom = 411;
	}
	
	@Override
	public void doDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		paint.setStyle(Style.FILL);
		canvas.drawRect(mBound, paint);
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(mBound, paint);
		
		// drawText调用会导致绘图效率降低许多，还不知道为什么。
//		String str = String.format("Speed %.2f", mBall.mBallData.mSpeed);
//		paint.setLinearText(true);
//		canvas.drawText(str, 30, 30, paint);
//		str = String.format("Angle %.2f", mBall.mBallData.mAngle);
//		canvas.drawText(str, 30, 50, paint);
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

	public void addBall(MapElementBall ball) {
		mBall = ball;
		this.addSubElement(ball);
	}
	
	public void updatePhysics() {
		mBall.calcNewPosition();
	}
	
	
}
