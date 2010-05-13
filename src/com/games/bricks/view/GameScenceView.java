package com.games.bricks.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.games.bricks.R;
import com.games.bricks.common.BallData;
import com.games.bricks.common.MapData;
import com.games.bricks.controls.GameRegion;
import com.games.bricks.controls.MapElementBall;

public class GameScenceView extends SurfaceView 
	implements SurfaceHolder.Callback
{
	private class GameThread extends Thread {
		
		private Drawable mBrick01Image;
		private Drawable mBrick02Image;
		private Drawable mBrick03Image;	
		private Drawable mBallImage;
		
		private SurfaceHolder mSurfaceHolder;
		
		private boolean mRun;
		private long mLastTime;
		
		public GameThread(SurfaceHolder surfaceHolder, Context context,
		        Handler handler) {
			mSurfaceHolder = surfaceHolder;
			mContext = context;
			Resources res = context.getResources();
			
			mBrick01Image = res.getDrawable(R.drawable.brick1);
			mBrick02Image = res.getDrawable(R.drawable.brick2);
			mBrick03Image = res.getDrawable(R.drawable.brick3);
			
			mBallImage = res.getDrawable(R.drawable.ball);
		}

		@Override
		public void run() {
			while(mRun) {
				Canvas canvas = null;
				try {
					long now = System.currentTimeMillis();
					long elapsed = (now-mLastTime)/100;
					if(elapsed<1) {
						Thread.sleep(30);
						continue;
					}
					
					mLastTime += elapsed*100;
					canvas = mSurfaceHolder.lockCanvas();
					synchronized (mSurfaceHolder)  {
						updatePhysics();
                       	doDrawGameScene(canvas); 
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if(canvas != null)
						mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		private void updatePhysics() {
			mGameRegion.updatePhysics();
		}
		

		private void doDrawGameScene(Canvas canvas) {
			//#ifdef OUT_PUT_LOG
			Log.w("mydraw", "doDrawGameScence start!");
			//#endif
			mGameRegion.doDraw(canvas);
			//#ifdef OUT_PUT_LOG
			Log.w("mydraw", "doDrawGameScence done!");
			//#endif
		}

		public void setRunning(boolean b) {
			mRun = b;
			mLastTime = System.currentTimeMillis();
		}
		
		public boolean doKeyDown(int keyCode, KeyEvent event) {
			boolean ret = true;
			if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				mGameRegion.mBall.beBigger();
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				mGameRegion.mBall.beSmaller();
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				mGameRegion.mBall.speedUp();
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				mGameRegion.mBall.speedDown();
			} else 
				ret = false;
			return ret;
		}
		
		public boolean doKeyUp(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
		
		public boolean doTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			return true;
		}


		
	}
	
	private int mMode;
	private Context mContext;
	private int mLevelNum;
	private ArrayList<MapData> mMapDatas;
	
	public final static int BRICK_TYPE_01 = 0;
	public final static int BRICK_TYPE_02 = 1;
	public final static int BRICK_TYPE_03 = 2;
	


	private GameThread mThread;
	private GameRegion mGameRegion;
//	private TextView mTextView;

//	private final int ID_BUTTON_BRICK01 = 0x01;
//	private MapEditRegion mMapEditRegion;
	
	public GameScenceView(Context context, int level) {
		super(context);
		SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);
		// 尝试通过代码在该View中添加一个子View并进行布局	
//		mTextView = new TextView(context);
//		LayoutParams params = new LayoutParams();
//		mTextView.setLayoutParams(params );
		
		mThread = new GameThread(holder, context, null);

		setFocusable(true); // make sure we get key events
		this.bringToFront();
		this.requestFocus();
//		setWillNotDraw(false);
		
//		MapDataProvider mdp = new MapDataProvider();
//		MapData mapData = mdp.readMapData(level);
		mGameRegion = new GameRegion();
		
		BallData bd = new BallData();
		bd.mAngle = (float)(Math.PI*2/3);
		bd.mPosX = 50;
		bd.mPosY = 50;
		bd.mSpeed = 2.0f;
		bd.mSize = 1;
		bd.mPosXf = bd.mPosX;
		bd.mPosYf = bd.mPosY;
		MapElementBall ball = MapElementBall.CREATOR.createElementFromData(bd);
		ball.setImage(mThread.mBallImage);
		mGameRegion.addBall(ball);
//		mGameRegion = new GameRagion(mapData);
		
//		mMapEditRegion = new MapEditRegion(mapDatas.get(level));
//		mMapEditRegion.setBound(9, 9, 301, 301);
	}
	
	public GameThread getThread() {
		return mThread;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return mThread.doKeyDown(keyCode, event);
//		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return mThread.doKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return mThread.doTouchEvent(event);
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        mThread.setRunning(true);
        mThread.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mThread.setRunning(false);
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

}
