package com.games.bricks.controls;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


/*
 *自绘控件基类 MapControl.MapElement
 */
public class MapElement {
	protected MapElement mParent;
	protected ArrayList<MapElement> mComponents;
	
	protected ArrayList<Drawable> mImages;
	protected int 	mImageIndex;
	protected Rect	mBound;
	
	public int childOffX;
	public int childOffY;
	
	public interface MapElementCreator<T,D> {
		public T createElementFromData(D d);
		//{			return null;		};
	}
	
	public MapElement() {
		mParent = null;
		mImages = new ArrayList<Drawable>();
		mComponents = new ArrayList<MapElement>();
		mBound = new Rect(0,0,0,0);
		mImageIndex = 0;
		childOffX = childOffY = 0;
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	public void setBound(Rect bound) {
		mBound.left = bound.left;
		mBound.top = bound.top;
		mBound.right = bound.right;
		mBound.bottom = bound.bottom;
	}
		
	public void setImage(Drawable image) {
		mImages.clear();
		mImages.add(image);
		mImageIndex = 0;
	}
	
	public void addImage(Drawable image) {
		mImages.add(image);
	}
	
	public void setBound(int left, int top, int width, int height) {
		mBound.set(left, top, left+width, top+height);
	}
	
	public void doDraw(Canvas canvas) {
		if(mImages!=null && mImages.size()>=mImageIndex+1) {
			Drawable dr = mImages.get(mImageIndex);
			Rect bound = new Rect(mBound);
			if(mParent!=null) {
				bound.offset(mParent.getBound().left+mParent.childOffX, mParent.getBound().top+mParent.childOffY);
			}
			if(dr!=null) {
				dr.setBounds(bound);
				dr.draw(canvas);
			}
		}
		
		int size = mComponents.size();
		for(int index=0; index<size; ++index) {
			mComponents.get(index).doDraw(canvas);
		}
	}
	
	public boolean isPointInBound(Point pt) {
		return mBound.contains(pt.x, pt.y);
	}
	
	public boolean isCollided(MapElement mapElement) {
		return Rect.intersects(this.mBound, mapElement.getBound());
	}
	
	public void setParent(MapElement ele) {
		mParent = ele;
	}
	
	public void addSubElement(MapElement ele) {
		//如果判断ele的bound是否在parent的bound范围之内的话，会导致一些特殊的类对象无法
		//正常添加到parent的子列表中。
//		Rect bound = new Rect(ele.getBound());
//		bound.offset(mBound.left+childOffX, mBound.top+childOffY);
//		if(mBound.contains(bound)) {
			ele.setParent(this);
			mComponents.add(ele);
//		}
	}
}
		
	
	
