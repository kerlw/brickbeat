package com.games.bricks.common;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * 从XML文件读出的砖块数据
 */
public class BallData implements Parcelable {
	public int mType;
	public int mPosX;
	public int mPosY;
	public float mPosXf;
	public float mPosYf;
	public float mSpeed;
	public float mAngle;
	public int mSize;

	public static final Parcelable.Creator<BallData> CREATOR
		= new Parcelable.Creator<BallData>() {
			public BallData createFromParcel(Parcel in) {
					return new BallData(in);
			}  
	        public BallData[] newArray(int size) {
	            return new BallData[size];
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
	
	private BallData(Parcel in) {
		mType = in.readInt();
		mPosX = in.readInt();
		mPosY = in.readInt();
	}
	
	public BallData() {
		mType = 0;
		mPosX = 0;
		mPosY = 0;
	}
}		
