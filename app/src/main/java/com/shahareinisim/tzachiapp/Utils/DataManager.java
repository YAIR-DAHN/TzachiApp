package com.shahareinisim.tzachiapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

@SuppressLint("Range")
public class DataManager {
    private SQLiteDatabase database;
    private final DBHelper dbHelper;

    public DataManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void openedTfilah(String name) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, name);

        database.beginTransaction();
        try {
            Cursor cursor = database.query(DBHelper.TABLE_TFILOT,
                    new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_OPEN_COUNT},
                    DBHelper.COLUMN_NAME + " = ?",
                    new String[]{name},
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));

                int visitCount = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_OPEN_COUNT));
                visitCount++;
                values.put(DBHelper.COLUMN_OPEN_COUNT, visitCount);

                database.update(DBHelper.TABLE_TFILOT, values,
                        DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            } else {
                database.insert(DBHelper.TABLE_TFILOT, null, values);
            }

            if (cursor != null) {
                cursor.close();
            }

            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public String getMostUsedTfilah() {
        String query = "SELECT " + DBHelper.COLUMN_NAME + " FROM " + DBHelper.TABLE_TFILOT +
                " ORDER BY " + DBHelper.COLUMN_OPEN_COUNT + " DESC LIMIT 1";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            String mostFrequentVisitor = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            cursor.close();
            return mostFrequentVisitor;
        }

        return null;
    }
}