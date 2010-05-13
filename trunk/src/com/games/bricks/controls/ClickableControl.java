package com.games.bricks.controls;

import android.graphics.Point;

/*
 *������ClickableContorl��ClickableControl.Callbak�ӿڣ�û�в���ϵͳ����Ϣ���ƣ�ֱ�Ӳ��ûص���ʽ
 *ֻ��Ϊ��ͼ�򵥣��Ժ���ԸĽ�ΪMessage/Handlerģʽ
 */
public interface ClickableControl {
	public abstract void onClick(Point pt);
	public abstract void addCallback(Callback callback);
	public interface Callback {
		public abstract void handleClick(ClickableControl control);
	}
}
