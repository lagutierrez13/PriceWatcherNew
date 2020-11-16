package edu.utep.cs.cs4330.mypricewatchernew;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
    Authors: Luis Gutierrez Antonio Zavala
    Class: CS4330
 */

public class ItemDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "itemDB";
    private static final String ITEM_TABLE = "items";

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_INIT_PRICE = "init_price";
    private static final String KEY_CURR_PRICE = "curr_price";
    private static final String KEY_URL = "url";
    private static final String KEY_DATE_ADDED = "date_added";
    private static final String KEY_SOURCE_NAME = "source_name";

    public ItemDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + ITEM_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_INIT_PRICE + " REAL, "
                + KEY_CURR_PRICE + " REAL, "
                + KEY_URL + " TEXT, "
                + KEY_DATE_ADDED + " TEXT, "
                + KEY_SOURCE_NAME + " TEXT " + ")";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        onCreate(database);
    }

    public void addItem(Item item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, item.getName());
            values.put(KEY_INIT_PRICE, item.getInitPrice());
            values.put(KEY_CURR_PRICE, item.getCurrentPrice());
            values.put(KEY_URL, item.getUrl());
            values.put(KEY_DATE_ADDED, item.getDateAdded());
            values.put(KEY_SOURCE_NAME, item.getSourceName());
            long id = db.insert(ITEM_TABLE, null, values);
            item.setId((int) id);
            db.close();
        } catch (SQLException e) {
            Log.e("Exception", "SQLException" + String.valueOf(e.getMessage()));
            e.printStackTrace();
        }
    }

    public List<Item> allItems() {
        List<Item> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double initPrice = cursor.getFloat(2);
                double currentPrice = cursor.getFloat(3);
                String url = cursor.getString(4);
                String dateAdded = cursor.getString(5);
                String sourceName = cursor.getString(6);

                Item item = new Item(id, name, initPrice, currentPrice, url, sourceName, dateAdded);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE, null, new String[]{});
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ITEM_TABLE, KEY_ID + " = " + id, null);
        db.close();
    }

    public void update(int id, Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_INIT_PRICE, item.getInitPrice());
        values.put(KEY_CURR_PRICE, item.getCurrentPrice());
        values.put(KEY_URL, item.getUrl());
        values.put(KEY_DATE_ADDED, item.getDateAdded());
        values.put(KEY_SOURCE_NAME, item.getSourceName());
        db.update(ITEM_TABLE, values, KEY_ID + " = " + id, new String[]{String.valueOf(item.getId())});
        db.close();
    }

}
