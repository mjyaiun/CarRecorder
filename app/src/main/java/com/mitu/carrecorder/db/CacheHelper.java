package com.mitu.carrecorder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.mitu.carrecorder.entiy.Device;

import java.util.ArrayList;

/**
 * 说明：缓存数据库
 * 2016/4/11 0011
 */
public class CacheHelper extends SQLiteOpenHelper{


    private static final String DB_NAME = "cache.db";
    public final static int VERSION = 1;


    private static final String TB_NAME_DEVICE = "device_info";
    private static final String MAC_INFO = "mac_address";
    private static final String ID = "device_id";
    private static final String DEVICE_NAME = "device_name";



    private static CacheHelper mDbHelper = null;
    private Context ctx;

    public static CacheHelper getDBHelper(Context context) {
        if (mDbHelper == null) {
            mDbHelper = new CacheHelper(context, DB_NAME, null, VERSION);
        }
        return mDbHelper;
    }

    private CacheHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //新建菜品分类信息缓存表
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(TB_NAME_DEVICE);
        sb.append(" (");
        sb.append(ID + " INTEGER PRIMARY KEY,");
        sb.append(DEVICE_NAME + " VARCHAR,");
        sb.append(MAC_INFO + " VARCHAR");
        sb.append(")");
        db.execSQL(sb.toString());




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 获取设备列表
     * @return
     */
    public ArrayList<Device> getDeviceList(){
        ArrayList<Device> deviceList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_NAME_DEVICE, null, null, null,
                null, null, null);
        while (c.moveToNext()) {
            Device device = new Device();
            device.device_id = c.getInt(c.getColumnIndex(ID));
            device.deviceName = c.getString(c.getColumnIndex(DEVICE_NAME));
            device.device_mac = c.getString(c.getColumnIndex(MAC_INFO));
            deviceList.add(device);
        }
        c.close();
        db.close();
        return deviceList;
    }


    public void deleteDevice(int id){
        SQLiteDatabase db = getReadableDatabase();
        String selection = ID + "=? ";
        String[] selectArgs = {String.valueOf(id)};
        db.delete(TB_NAME_DEVICE,selection,selectArgs);
        db.close();

    }


    /**
     * 添加设备
     * @param name
     * @param macStr
     */
    public void addDevice(String name,String macStr){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DEVICE_NAME,name);
        value.put(MAC_INFO,macStr);
        db.insert(TB_NAME_DEVICE,null,value);
    }


    /***
     * 检查缓存中是否有当前连接的设备
     * @param mac
     * @return
     */
    public boolean isHasCurrent(String mac){
        boolean isHas = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TB_NAME_DEVICE, null, null, null,
                null, null, null);
        while (c.moveToNext()){
            String macStr = c.getString(c.getColumnIndex(MAC_INFO));
            if (macStr.equals(mac)){
                isHas = true;
                break;
            }
        }
        c.close();
        db.close();
        return isHas;
    }


}
