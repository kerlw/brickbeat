package com.games.bricks.controls;

import android.graphics.Point;

/*
 *定义了ClickableContorl和ClickableControl.Callbak接口，没有采用系统的消息机制，直接采用回调方式
 *只是为了图简单，以后可以改进为Message/Handler模式
 */
public interface ClickableControl {
	public abstract void onClick(Point pt);
	public abstract void addCallback(Callback callback);
	public interface Callback {
		public abstract void handleClick(ClickableControl control);
	}
}
