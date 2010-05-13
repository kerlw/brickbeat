package com.games.bricks.common;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * 从XML文件读出的地图数据，实际为Brick数据的ArrayList
 */
public class MapData implements Parcelable {
	//toString方法的格式化字符串，形如"Lv.01     23 bricks."
	private final static String STR_LIST_ROW = "Lv.%02d%10d bricks.";

	public int mLevel;
	public ArrayList<BrickData> mBricks;
	public MapData() {
		mBricks = new ArrayList<BrickData>();
	}
	public String toString() {
		String s = String.format(STR_LIST_ROW , mLevel, mBricks.size());
		return s;
	}
	
	public static final Parcelable.Creator<MapData> CREATOR
    		= new Parcelable.Creator<MapData>() {
				public MapData createFromParcel(Parcel in) {
						return new MapData(in);
				}  
		        public MapData[] newArray(int size) {
		            return new MapData[size];
		        }
			};

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mBricks.size());
		for(int index = 0; index < mBricks.size(); index++) {
			mBricks.get(index).writeToParcel(dest, flags);
		}
		
	}
	
	private MapData(Parcel in) {
		mBricks = new ArrayList<BrickData>();
		int count = in.readInt();
		for(int i = 0; i < count; ++i){
			mBricks.add( BrickData.CREATOR.createFromParcel(in) );
		}
	}
}