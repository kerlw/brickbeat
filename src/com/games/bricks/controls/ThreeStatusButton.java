package com.games.bricks.controls;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


/*
 * �������д�����һ���Ի�ؼ��� MapControl.MapElement
 * ��������һ����̬��ť ThreeStatusButton���ڵ�ͼ�༭���Ĺ�������ť
 */
interface FocusableControl {
	public abstract void onFocus();
}

public class ThreeStatusButton extends MapElement 
		implements FocusableControl, ClickableControl 
{
	public final static int STATUS_UP = 0;
	public final static int STATUS_DOWN = 1;
	public final static int STATUS_FOCUS = 2;
	
	private ClickableControl.Callback mCallback;
		
	public int getStatus() {
		return mImageIndex;
	}

	public void setStatus(int status) {
		mImageIndex = status;
		if(mImageIndex==STATUS_DOWN) {
			childOffX = 4;	childOffY = 4;
		} else {
			childOffX = 0;	childOffY = 0;
		}
	}

	public void initImages(Drawable imgUp, Drawable imgDown, Drawable imgFocus) {
		mImages.clear();
		//ע�⣬�����˳���ܴ���
		mImages.add(STATUS_UP, imgUp);
		mImages.add(STATUS_DOWN, imgDown);
		mImages.add(STATUS_FOCUS, imgFocus);
		
		mImageIndex = STATUS_UP;
	}

	@Override
	public void onFocus() {
		mImageIndex = STATUS_FOCUS;
	}

	@Override
	public void onClick(Point pt) {
//		if(mImageIndex==STATUS_DOWN) {
//			setStatus(STATUS_FOCUS);
//		} else {
//			setStatus(STATUS_DOWN);
//		}
		if(mCallback!=null)
			mCallback.handleClick(this);
	}

	@Override
	public void addCallback(Callback callback) {
		mCallback = callback;
	}

	public void addIcon(Drawable iconImage, Rect iconRect) {
		MapElement icon = new MapElement();
		icon.setImage(iconImage);
		icon.setBound(iconRect);
		addSubElement(icon);	
	}
}
	
