package com.games.bricks.common;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * 从XML文件读出的砖块数据
 */
public class BrickData implements Parcelable {
	public int mType;
	public int mPosX;
	public int mPosY;

	public static final Parcelable.Creator<BrickData> CREATOR
		= new Parcelable.Creator<BrickData>() {
			public BrickData createFromParcel(Parcel in) {
					return new BrickData(in);
			}  
	        public BrickData[] newArray(int size) {
	            return new BrickData[size];
	        }
		};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mType);
		dest.writeInt(mPosX);
		dest.writeInt(mPosY);
	}
	
	private BrickData(Parcel in) {
		mType = in.readInt();
		mPosX = in.readInt();
		mPosY = in.readInt();
	}
	
	public BrickData() {
		mType = 0;
		mPosX = 0;
		mPosY = 0;
	}
}		
