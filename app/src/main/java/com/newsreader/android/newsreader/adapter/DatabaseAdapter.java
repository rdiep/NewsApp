package com.newsreader.android.newsreader.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

public class DatabaseAdapter {

    DatabaseHelper helper;

    public DatabaseAdapter(Context context){
        helper = new DatabaseHelper(context);
    }

    public long insertData(String name, String topic){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TAB_NAME, name);
        contentValues.put(DatabaseHelper.SEARCH_NAME, topic);
        long id=db.insert(DatabaseHelper.TABLE_NAME2,null,contentValues);
        return id;
    }

    public long deleteData(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_NAME2, "TabName = ?", new String[] {name});
    }

    public String getTopic(String name){
        String topic = "";
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.TAB_NAME, DatabaseHelper.SEARCH_NAME};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME2, columns, "TabName = ?", new String[] {name}, null, null, null);
        while(cursor.moveToNext()){
            Log.i("STTRING",cursor.getString(0));
            Log.i("STTRING2",cursor.getString(1));
            topic = cursor.getString(1);
        }
        return topic;
    }

    public List<String> getTabNames(List<String> list1){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.TAB_NAME, DatabaseHelper.SEARCH_NAME};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME2, columns, null, null, null, null, null);
        while(cursor.moveToNext()){
            Log.i("STTRING",cursor.getString(0));
            Log.i("STTRING2",cursor.getString(1));
            list1.add(cursor.getString(0));
        }
        return list1;
    }

   static class DatabaseHelper extends SQLiteOpenHelper {
       private static final String DATABASE_NAME="newsdatabase";
       private static final String TABLE_NAME="TABS";
       private static final int DATABASE_VERSION=9;
       private static final String TAB_NAME = "TabName";
       private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ("+TAB_NAME+" TEXT PRIMARY KEY);";
       private static final String TABLE_NAME2="SEARCH";
       private static final String SEARCH_NAME = "Search";
       private static final String CREATE_TABLE2="CREATE TABLE "+TABLE_NAME2+" ("+TAB_NAME+" TEXT PRIMARY KEY, "+SEARCH_NAME+" TEXT);";

       private static final String DROP1 = "DROP TABLE IF EXISTS " + TABLE_NAME+";";
       private static final String DROP2 = "DROP TABLE IF EXISTS " + TABLE_NAME2+";";
       private Context context;

       public DatabaseHelper(Context context){
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
           this.context = context;
       }

       @Override
       public void onCreate(SQLiteDatabase sqLiteDatabase) {
           try {
               Log.i("onCreate","gsdgsd");
               sqLiteDatabase.execSQL(CREATE_TABLE2);
           }catch (SQLException e){
               e.printStackTrace();
           }
       }

       @Override
       public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
           try{
               Log.i("onUpgrade","gsdgsd");
               sqLiteDatabase.execSQL(DROP2);
               onCreate(sqLiteDatabase);
           }catch (SQLException e){
               e.printStackTrace();
           }
       }
   }
}
