package com.msomu.handysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by msomu on 03/03/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "smsStructureDB";

    // Contacts table name
    private static final String TABLE_PROVIDER_NAME = "provider_name";
    private static final String TABLE_SENDER_CODE = "sender_code";
    private static final String TABLE_TEMPLATE = "template";
    private static final String TABLE_SCENARIO = "scenario";

    private static final String KEY_ID = "_id";
    private static final String KEY_PROVIDER_NAME = "provider_name";
    private static final String KEY_SENDER_CODE = "sender_name";
    private static final String KEY_SENDER_ID = "sender_id";
    private static final String KEY_TEMPLATE = "template";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SENDER_NAME_TABLE = "CREATE TABLE " + TABLE_PROVIDER_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROVIDER_NAME + " TEXT)";
        String CREATE_SENDER_CODE_TABLE = "CREATE TABLE " + TABLE_SENDER_CODE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SENDER_ID + "INTEGER" + KEY_SENDER_CODE + " TEXT)";
        String CREATE_TEMPLATE_TABLE = "CREATE TABLE " + TABLE_TEMPLATE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SENDER_ID + "INTEGER" + KEY_TEMPLATE + " TEXT)";
        String CREATE_SCENARIO_TABLE = "CREATE TABLE " + TABLE_SCENARIO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ID + "INTEGER" + KEY_TEMPLATE + " TEXT)";

        db.execSQL(CREATE_SENDER_NAME_TABLE);
        db.execSQL(CREATE_SENDER_CODE_TABLE);
        db.execSQL(CREATE_TEMPLATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVIDER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDER_CODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLATE);
        onCreate(db);
    }

    public void addSenderName(SenderName senderName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROVIDER_NAME, senderName.getSenderName());
        db.insert(TABLE_PROVIDER_NAME, null, values);
        db.close();
    }

    public void addSenderCode(SenderCode senderCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SENDER_CODE, senderCode.getSenderCode());
        values.put(KEY_SENDER_ID, senderCode.getSenderId());
        db.insert(TABLE_SENDER_CODE, null, values);
        db.close();
    }

    public void addTemplate(SMSTemplate smsTemplate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEMPLATE, smsTemplate.getTemplate());
        values.put(KEY_SENDER_ID, smsTemplate.getSenderId());
        db.insert(TABLE_TEMPLATE, null, values);
        db.close();
    }
}
