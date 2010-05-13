package com.games.bricks.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;


public class MapDataProvider /*extends ContentProvider */{
	
	public final static String MAP_LIST_FILE_NAME = "maplist";
	public final static String MAP_LEVEL_FILE_NAME_PREFIX = "level";
	private String mFilePath;
	private ArrayList<MapData> mMapDatas;
	
	public final ArrayList<MapData> getMapDatas() {
		return mMapDatas;
	}
	


	private class MapDataHelper {
		private class DataParser {
			private byte[] mBuffer;
			private int mPos;
			
			public DataParser(byte[] buffer) {
				mBuffer = buffer;
				mPos = 0;
			}
			
			public void resetPos() { mPos = 0;	}
			public byte readByte() {return mPos >= mBuffer.length ? 0 : mBuffer[mPos++];}
			public int writeByte(byte b) {	
				if(mPos<mBuffer.length) mBuffer[mPos++] = b;
				return mPos;
			}
			public int readInt() {
				int tmp = 0;
				if (mPos+4 >= mBuffer.length)
					return tmp;
				
				tmp = mBuffer[mPos] + (mBuffer[mPos+1]<<8) + (mBuffer[mPos+2]<<16) + (mBuffer[mPos+3]<<24);
				mPos += 4;
				return tmp;
			}
			public int writeInt(int value) {
				if (mPos+4 >= mBuffer.length)
					return mPos;
				
				mBuffer[mPos++] = (byte) (value);
				mBuffer[mPos++] = (byte) (value>>8);
				mBuffer[mPos++] = (byte) (value>>16);
				mBuffer[mPos++] = (byte) (value>>24);
				
				return mPos;
			}
		}
		
		public void readMapData() {
			mMapDatas.clear();
			int nums = getLevelNums();
			String fileName;
			for (int i=0; i<nums; ++i) {
				fileName = String.format("%s/%s%02d", mFilePath, MAP_LEVEL_FILE_NAME_PREFIX, i+1);
				MapData data = readMapDataFromFile(fileName);
				data.mLevel = i+1;
				mMapDatas.add(data);
			}
		}
		
		private MapData readMapDataFromFile(String fileName) {
			MapData mapData = new MapData();
			try {
				FileInputStream fi = new FileInputStream(fileName);
				byte [] buffer = new byte[2048];	
				int length = fi.read(buffer);
				if(length<4)
					return mapData;
				
				DataParser parser = new DataParser(buffer);
				int brickNum = parser.readInt();
				for(int i=0;i<brickNum;++i) {
					BrickData brick = new BrickData();
					brick.mType = parser.readByte();
					brick.mPosX = parser.readByte();
					brick.mPosY = parser.readByte();
					mapData.mBricks.add(brick);
				}
				parser = null;
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mapData;
		}
		
		private void saveMapDataToFile(MapData data, String fileName) {
			try {
				FileOutputStream fo = new FileOutputStream(fileName);
				byte[] buffer = new byte[2048];
				DataParser parser = new DataParser(buffer);
				int num = data.mBricks.size();
				int len = parser.writeInt(num);
				
				for(int i=0; i<num; ++i) {
					BrickData brick = data.mBricks.get(i);
					len = parser.writeByte( (byte)brick.mType );
					len = parser.writeByte( (byte)brick.mPosX );
					len = parser.writeByte( (byte)brick.mPosY );
				}
				
				fo.write(buffer, 0, len);
				fo.flush();
				fo.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private int getLevelNums() {
			try {
				FileInputStream fi = new FileInputStream(mFilePath+"/"+MAP_LIST_FILE_NAME);
				byte [] buffer = new byte[1024];
				int length = fi.read(buffer);
				if(length<4)
					return 0;
				
				DataParser parser = new DataParser(buffer);
				int ret = parser.readInt();
				parser = null;
				return ret;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
		}
		
		public void updateLevelNums(int num) {
			byte[] buffer = new byte[4];
			DataParser parser = new DataParser(buffer);
			parser.writeInt(num);
			
			try {
				FileOutputStream fo = new FileOutputStream(mFilePath+"/"+MAP_LIST_FILE_NAME, false);
				fo.write(buffer);
				fo.flush();
				fo.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private MapDataHelper mHelper;
	private Context mContext;
	
	public MapDataProvider(Context context) {
		mContext = context;
		mMapDatas = new ArrayList<MapData>();
		mHelper = new MapDataHelper();
		mFilePath = context.getFilesDir().getPath();
	}
	
	public final void readMapData(){
		mHelper.readMapData();
	}
	
	public final void updateMapData(MapData data, int levelNum) {
		if (levelNum >= mHelper.getLevelNums()) {
			mHelper.updateLevelNums(levelNum+1);
		}
		
		String fileName = String.format("%s/%s%02d", mFilePath, MAP_LEVEL_FILE_NAME_PREFIX, levelNum+1);
		mHelper.saveMapDataToFile(data, fileName);
//		mHelper.
	}
//	@Override  
//	public int delete(Uri uri, String selection, String[] selectionArgs) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public String getType(Uri uri) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Uri insert(Uri uri, ContentValues values) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean onCreate() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Cursor query(Uri uri, String[] projection, String selection,
//			String[] selectionArgs, String sortOrder) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int update(Uri uri, ContentValues values, String selection,
//			String[] selectionArgs) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	
	/*
	 * 以xml实现的map格式，读写过于复杂，效率低，放弃。
	 */
	/*
	private class MapDataHelper {
		//地图xml文件名
		private final String STR_MAP_FILE_NAME = "maps.xml";
		//Map节点的TagName
		private final String STR_TAG_LEVEL;
		//Map节点的属性Level
		private final String STR_MAP_ATTR_LEVEL;
		//Brick节点的TagName
		private final String STR_TAG_BRICK;
		//Brick节点属性Type
		private final String STR_BRICK_ATTR_TYPE;
		//Brick节点属性PosX
		private final String STR_BRICK_ATTR_POSX;
		//Brick节点属性PosY
		private final String STR_BRICK_ATTR_POSY;
		

		
		public MapDataHelper() {
			Resources res = mContext.getResources();
			//从资源读取常量字符串
			STR_TAG_LEVEL = res.getString(R.string.level_tag);
			STR_TAG_BRICK = res.getString(R.string.brick_tag);
			STR_BRICK_ATTR_TYPE = res.getString(R.string.brick_node_type);
			STR_BRICK_ATTR_POSX = res.getString(R.string.brick_node_posx);
			STR_BRICK_ATTR_POSY = res.getString(R.string.brick_node_posy);	
			STR_MAP_ATTR_LEVEL = res.getString(R.string.map_node_level);
		}
		
		public void readMapData() {
			DocumentBuilderFactory docBuilderFactory = null;
			DocumentBuilder	docBuilder = null;
			Document	doc = null;
			Resources res = mContext.getResources();
			
			try {
				//初始化DOM
				docBuilderFactory = DocumentBuilderFactory.newInstance();
				docBuilder = docBuilderFactory.newDocumentBuilder();
				doc = docBuilder.parse(res.getAssets().open( STR_MAP_FILE_NAME ));
				
				// 取得根节点，再从中取出符合条件的子节点集合
				Element root = doc.getDocumentElement();
				NodeList levelList = root.getElementsByTagName( STR_TAG_LEVEL );
				int levelNums = levelList.getLength();

				//使用局部变量替换成员变量，优化效率
				ArrayList<MapData> mapDataList = mMapDatas;
				
				//遍历关卡节点，读取每一关卡的数据
				for(int i=0;i<levelNums;++i) {
					Node ndMap = levelList.item(i);
					MapData mapData = new MapData();
					//从Map节点属性Level中读取关卡索引值
					mapData.mLevel = Integer.parseInt(ndMap.getAttributes().getNamedItem( STR_MAP_ATTR_LEVEL ).getNodeValue());
					NodeList brickList = ndMap.getChildNodes();
					int brickNums = brickList.getLength();
					for(int j=0;j<brickNums;++j) {
						Node ndBrick = brickList.item(j);
						if(!STR_TAG_BRICK.equalsIgnoreCase(ndBrick.getNodeName()))
							continue;
						
						BrickData brickData = new BrickData();
						NamedNodeMap ndBrickAttr = ndBrick.getAttributes();
						brickData.mType = Integer.parseInt(ndBrickAttr.getNamedItem( STR_BRICK_ATTR_TYPE ).getNodeValue());
						brickData.mPosX = Integer.parseInt(ndBrickAttr.getNamedItem( STR_BRICK_ATTR_POSX ).getNodeValue());
						brickData.mPosY = Integer.parseInt(ndBrickAttr.getNamedItem( STR_BRICK_ATTR_POSY ).getNodeValue());
						
						mapData.mBricks.add(brickData);
					}
					
					mapDataList.add(mapData);
				}
				
			} catch (Exception e) {
				Log.e("mydebug", "Exception msg!");
			} finally {
				doc = null;
				docBuilder = null;
				docBuilderFactory = null;
			}
		}
	}
	*/
}
