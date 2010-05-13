package com.games.bricks.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.games.bricks.R;
import com.games.bricks.common.MapData;
import com.games.bricks.controls.ClickableControl;
import com.games.bricks.controls.MapEditRegion;
import com.games.bricks.controls.MapElement;
import com.games.bricks.controls.MapLayerBrick;
import com.games.bricks.controls.ThreeStatusButton;

public class MapEditView extends SurfaceView 
	implements SurfaceHolder.Callback, ClickableControl.Callback
{
	public final static int NEW_BRICK_TYPE_01 = 0;
	public final static int NEW_BRICK_TYPE_02 = 1;
	public final static int NEW_BRICK_TYPE_03 = 2;
	public final static int NEW_BRICK_TYPE_ERASE = 3;
	public final static int NEW_BRICK_TYPE_NONE = 4;
	
	private Context mContext;
	private MapData mMapData;
	private int mToolType;
	
	private Drawable mBrick01Image;
	private Drawable mBrick02Image;
	private Drawable mBrick03Image;	
	private Drawable mDelBrickImage;
	private Drawable mToolBtnUpImage;
	private Drawable mToolBtnDownImage;
	
	private ThreeStatusButton mBtnToolBrick1;
	private ThreeStatusButton mBtnToolBrick2;
	private ThreeStatusButton mBtnToolBrick3;
	private ThreeStatusButton mBtnToolDelete;

	private MapEditRegion mMapEditRegion;
	private MapLayerBrick mMapLayerBrick;
	
	public MapEditView(Context context, MapData mapData) {
		super(context);
		mMapData = mapData;
		
		mToolType = NEW_BRICK_TYPE_NONE;
		
		setFocusable(true); // make sure we get key events
		Resources res = context.getResources();
		
		mBrick01Image = res.getDrawable(R.drawable.brick1);
		mBrick02Image = res.getDrawable(R.drawable.brick2);
		mBrick03Image = res.getDrawable(R.drawable.brick3);
		mDelBrickImage = res.getDrawable(R.drawable.delbrick);
		mToolBtnUpImage = res.getDrawable(R.drawable.toolbtnup);
		mToolBtnDownImage = res.getDrawable(R.drawable.toolbtndown);
		
		SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);
		setWillNotDraw(false);
		
		mBtnToolBrick1 = new ThreeStatusButton();
		mBtnToolBrick1.addCallback(this);
		mBtnToolBrick1.setBound(10, 320, 60, 60);
		mBtnToolBrick1.initImages(mToolBtnUpImage, mToolBtnDownImage, mToolBtnUpImage);
		mBtnToolBrick1.addIcon(mBrick01Image, new Rect(7, 17, 47, 37));
		
		mBtnToolBrick2 = new ThreeStatusButton();
		mBtnToolBrick2.addCallback(this);
		mBtnToolBrick2.setBound(90, 320, 60, 60);
		mBtnToolBrick2.initImages(mToolBtnUpImage, mToolBtnDownImage, mToolBtnUpImage);
		mBtnToolBrick2.addIcon(mBrick02Image, new Rect(7, 17, 47, 37));
	
		mBtnToolBrick3 = new ThreeStatusButton();
		mBtnToolBrick3.addCallback(this);
		mBtnToolBrick3.setBound(170, 320, 60, 60);
		mBtnToolBrick3.initImages(mToolBtnUpImage, mToolBtnDownImage, mToolBtnUpImage);
		mBtnToolBrick3.addIcon(mBrick03Image, new Rect(7, 17, 47, 37));
	
		mBtnToolDelete = new ThreeStatusButton();
		mBtnToolDelete.addCallback(this);
		mBtnToolDelete.setBound(250, 320, 60, 60);
		mBtnToolDelete.initImages(mToolBtnUpImage, mToolBtnDownImage, mToolBtnUpImage);
		mBtnToolDelete.addIcon(mDelBrickImage, new Rect(7, 7, 47, 47));
	
		
		mMapEditRegion = new MapEditRegion();
		mMapEditRegion.setBound(9, 9, 302, 302);
		mMapLayerBrick = new MapLayerBrick();
		mMapLayerBrick.setBound(10, 10, 300, 300);
		mMapLayerBrick.addBrickType(mBrick01Image, NEW_BRICK_TYPE_01);
		mMapLayerBrick.addBrickType(mBrick02Image, NEW_BRICK_TYPE_02);
		mMapLayerBrick.addBrickType(mBrick03Image, NEW_BRICK_TYPE_03);
		mMapLayerBrick.initBricks(mapData);
		
		mMapEditRegion.addSubElement(mMapLayerBrick);
	}
	
	public MapData getMapData() {
		return mMapData;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		Point pt = new Point(x,y);
		if(mBtnToolBrick1.isPointInBound(pt))
			mBtnToolBrick1.onClick(pt);
		else if(mBtnToolBrick2.isPointInBound(pt))
			mBtnToolBrick2.onClick(pt);
		else if(mBtnToolBrick3.isPointInBound(pt))
			mBtnToolBrick3.onClick(pt);
		else if(mBtnToolDelete.isPointInBound(pt))
			mBtnToolDelete.onClick(pt);
		else if(mMapEditRegion.isPointInBound(pt)) {
			
			if (mToolType==NEW_BRICK_TYPE_ERASE) {
				Rect rc = mMapLayerBrick.removeBrickAt(pt);
				if(rc!=null)
					this.invalidate(rc);
				this.invalidate();
			} else if (mToolType!=NEW_BRICK_TYPE_NONE) {
				Rect rc = mMapLayerBrick.addBrickAt(pt, mToolType);
				if(rc!=null)
					this.invalidate(rc);
			}
			
		}
		return super.onTouchEvent(event);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		doDrawMapEditor(canvas);
		super.onDraw(canvas);
	}
	
	private void doDrawMapEditor(Canvas canvas) {
		//绘制地图编辑区域
		mMapEditRegion.doDraw(canvas);
	
		//绘制工具栏
		mBtnToolBrick1.doDraw(canvas);
		mBtnToolBrick2.doDraw(canvas);
		mBtnToolBrick3.doDraw(canvas);
		mBtnToolDelete.doDraw(canvas);
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.invalidate();
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mMapData.mBricks.clear();
		//mMapData.mBricks.
		
	}
	/*
	 * 结构没设计好，下面的函数内容包含很多重复代码，可以设计一个ToolBar类来处理按钮点击
	 * 保持单个按钮为按下状态的逻辑
	 */
	@Override
	public void handleClick(ClickableControl control) {
		//1号砖被点击
		if(control==mBtnToolBrick1) {
			if(mToolType!=NEW_BRICK_TYPE_01) {
				mBtnToolBrick1.setStatus(ThreeStatusButton.STATUS_DOWN);
				mBtnToolBrick2.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolBrick3.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolDelete.setStatus(ThreeStatusButton.STATUS_UP);
				mToolType = NEW_BRICK_TYPE_01;
			} else {
				mBtnToolBrick1.setStatus(ThreeStatusButton.STATUS_FOCUS);
				mToolType = NEW_BRICK_TYPE_NONE;
			}
		} 
		//2号砖按钮被点击
		else if(control==mBtnToolBrick2) {
			if(mToolType!=NEW_BRICK_TYPE_02) {
				mBtnToolBrick1.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolBrick2.setStatus(ThreeStatusButton.STATUS_DOWN);
				mBtnToolBrick3.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolDelete.setStatus(ThreeStatusButton.STATUS_UP);
				mToolType = NEW_BRICK_TYPE_02;
			} else {
				mBtnToolBrick2.setStatus(ThreeStatusButton.STATUS_FOCUS);
				mToolType = NEW_BRICK_TYPE_NONE;
			}
		} 
		//3号砖按钮被点击
		else if(control==mBtnToolBrick3) {
			if(mToolType!=NEW_BRICK_TYPE_03) {
				mBtnToolBrick1.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolBrick2.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolBrick3.setStatus(ThreeStatusButton.STATUS_DOWN);
				mBtnToolDelete.setStatus(ThreeStatusButton.STATUS_UP);
				mToolType = NEW_BRICK_TYPE_03;
			} else {
				mBtnToolBrick3.setStatus(ThreeStatusButton.STATUS_FOCUS);
				mToolType = NEW_BRICK_TYPE_NONE;
			}
		} 
		//删除按钮被点击
		else if(control==mBtnToolDelete) {
			if(mToolType!=NEW_BRICK_TYPE_ERASE) {
				mBtnToolBrick1.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolBrick2.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolBrick3.setStatus(ThreeStatusButton.STATUS_UP);
				mBtnToolDelete.setStatus(ThreeStatusButton.STATUS_DOWN);
				mToolType = NEW_BRICK_TYPE_ERASE;
			} else {
				mBtnToolDelete.setStatus(ThreeStatusButton.STATUS_FOCUS);
				mToolType = NEW_BRICK_TYPE_NONE;
			}
		} 
//		if(control.getClass().equals(ThreeStatusButton.class))
			this.invalidate();
		
	}

}
