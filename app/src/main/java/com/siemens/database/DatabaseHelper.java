package com.siemens.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "User.db";
    public static final String TABLE_NAME = "user_table";
    public static final String COLUMN_MAIL = "mail";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PRIVILEGE = "privilege";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_MAIL + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_SURNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_PRIVILEGE + " INTEGER NOT NULL DEFAULT 0);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /********************************************************************************************************************/

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MAIL, user.get_mail());
        contentValues.put(COLUMN_NAME, user.get_name());
        contentValues.put(COLUMN_SURNAME, user.get_surname());
        contentValues.put(COLUMN_PASSWORD, user.get_password());
        contentValues.put(COLUMN_PRIVILEGE, user.get_privilege());
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COLUMN_MAIL, user.get_mail());
        contentValues.put(COLUMN_NAME, user.get_name());
        contentValues.put(COLUMN_SURNAME, user.get_surname());
        contentValues.put(COLUMN_PRIVILEGE, user.get_privilege());
        db.update(TABLE_NAME, contentValues, COLUMN_MAIL + " = " + "'" + user.get_mail() + "'", null);
    }

    public void updatePassword (User user, String _userMail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_PASSWORD, user.get_password());
        db.update(TABLE_NAME, contentValues, COLUMN_MAIL + " = " + "'" + _userMail + "'", null);

    }

    public void deleteUser (String mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_MAIL + " = " + "'" + mail + "'", null);
    }

    public Cursor getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    public Cursor getAllSuperior() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PRIVILEGE + " IS " + 1 + ";", null);
        return cursor;
    }

    public Cursor getOneUser(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MAIL + " IS " + "'" + mail + "';", null);
        return cursor;
    }
}
