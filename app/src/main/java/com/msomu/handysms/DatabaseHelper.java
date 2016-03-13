package com.msomu.handysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.msomu.handysms.models.ProviderModel;
import com.msomu.handysms.models.SenderModel;
import com.msomu.handysms.models.SmsDataClass;

import java.util.ArrayList;

/**
 * Created by msomu on 03/03/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "smsStructureDB";

    // Contacts table name
    private static final String PROVIDER_TABLE = "provider_table";
    private static final String SENDER_TABLE = "sender_table";
    private static final String SENDER_PROVIDER_TABLE = "sender_provider_table";
    private static final String SMS_TABLE = "sms_table";
    private static final String TEMPLATE_TABLE = "template_table";

    private static final String ID = "_id";
    private static final String PROVIDER_NAME = "provider_name";
    private static final String SENDER_CODE = "sender_code";
    private static final String SENDER_ID = "sender_id";
    private static final String PROVIDER_ID = "provider_id";
    private static final String SMS = "sms";
    private static final String DATE = "date_time";
    private static final String TEMPLATE = "template";

    private static final String CREATE_PROVIDER = "CREATE TABLE " + PROVIDER_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PROVIDER_NAME + " TEXT NOT NULL);";
    private static final String CREATE_SENDER = "CREATE TABLE " + SENDER_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SENDER_CODE + " TEXT NOT NULL);";
    private static final String CREATE_SENDER_PROVIDER = "CREATE TABLE " + SENDER_PROVIDER_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SENDER_ID + " INTEGER NOT NULL," + PROVIDER_ID + " INTEGER NOT NULL);";
    private static final String CREATE_SMS = "CREATE TABLE " + SMS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SENDER_ID + " INTEGER NOT NULL," + SMS + " TEXT NOT NULL, " + DATE + " TIMESTAMP NOT NULL);";
    private static final String CREATE_TEMPLATE = "CREATE TABLE " + TEMPLATE_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TEMPLATE + " TEXT NOT NULL);";
    private static final String TAG = DatabaseHelper.class.getSimpleName();


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, CREATE_PROVIDER);
        Log.d(TAG, CREATE_SENDER);
        Log.d(TAG, CREATE_SENDER_PROVIDER);
        Log.d(TAG, CREATE_SMS);
        Log.d(TAG, CREATE_TEMPLATE);

        db.execSQL(CREATE_PROVIDER);
        db.execSQL(CREATE_SENDER);
        db.execSQL(CREATE_SENDER_PROVIDER);
        db.execSQL(CREATE_SMS);
        db.execSQL(CREATE_TEMPLATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROVIDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SENDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SENDER_PROVIDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TEMPLATE_TABLE);
        onCreate(db);
    }

    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + SMS_TABLE);
        db.execSQL(CREATE_SMS);
    }

    /*
    * Creating a provider
    */
    public long createProvider(ProviderModel providerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROVIDER_NAME, providerModel.getProvider());
        // insert row
        return db.insert(PROVIDER_TABLE, null, values);
    }

    public long createSMS(String body, long senderId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SMS, body);
        values.put(SENDER_ID, senderId);
        values.put(DATE, date);
        // insert row
        return db.insert(SMS_TABLE, null, values);
    }

    public ArrayList<ProviderModel> getAllProviders() {
        ArrayList<ProviderModel> providers = new ArrayList<ProviderModel>();
        String selectQuery = "SELECT  * FROM " + PROVIDER_TABLE;

        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProviderModel td = new ProviderModel();
                td.setId(c.getInt((c.getColumnIndex(ID))));
                td.setProvider((c.getString(c.getColumnIndex(PROVIDER_NAME))));
                providers.add(td);
            } while (c.moveToNext());
        }
        c.close();
        return providers;
    }

    public long getSenderID(String addr) {

        String selectQuery = "SELECT " + ID + " FROM " + SENDER_TABLE + " WHERE " + SENDER_CODE + " = " + "'" + addr + "'";

        Log.d(TAG, selectQuery);
        long id = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            id = c.getInt(c.getColumnIndex(ID));
        }
        c.close();
        return id;
    }

    /*
    * Inserting a sender
    */
    public long createSender(SenderModel senderModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SENDER_CODE, senderModel.getSender());
        // insert row
        return db.insert(SENDER_TABLE, null, values);
    }

    /*
   * Map a sender and provider realtionship
   */
    public long mapSenderProvider(long senderId, long providerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SENDER_ID, senderId);
        values.put(PROVIDER_ID, providerId);
        // insert row
        return db.insert(SENDER_PROVIDER_TABLE, null, values);
    }

    public ArrayList<SmsDataClass> getAllSmsOfProvider(int providerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<SmsDataClass> smsDataClasses = new ArrayList<>();
        //SELECT     sms_table.sms,              sms_table.date_time,            provider_table.provider_name             FROM     sms_table      JOIN  sender_table ON sms_table.sender_id = sender_table._id                                    JOIN sender_provider_table ON sender_provider_table.sender_id = sender_table._id JOIN provider_table ON provider_table._id = sender_provider_table.provider_id WHERE sender_provider_table.provider_id=1;
        String QUERY = "SELECT " + SMS_TABLE + "." + SMS + ", " + SMS_TABLE + "." + DATE + ", " + SENDER_TABLE + "." + SENDER_CODE + " FROM " + SMS_TABLE + " JOIN " + SENDER_TABLE + " ON " + SMS_TABLE + "." + SENDER_ID + " = " + SENDER_TABLE + "." + ID + " JOIN " + SENDER_PROVIDER_TABLE + " ON " + SENDER_PROVIDER_TABLE + "." + SENDER_ID + " = " + SENDER_TABLE + "." + ID + " JOIN " + PROVIDER_TABLE + " ON " + PROVIDER_TABLE + "." + ID + " = " + SENDER_PROVIDER_TABLE + "." + PROVIDER_ID + " WHERE " + SENDER_PROVIDER_TABLE + "." + PROVIDER_ID + "=" + providerId + ";";
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                SmsDataClass sms = new SmsDataClass();
                sms.setDate((cursor.getString(cursor.getColumnIndex(DATE))));
                sms.setAddress((cursor.getString(cursor.getColumnIndex(SENDER_CODE))));
                sms.setBody((cursor.getString(cursor.getColumnIndex(SMS))));
                smsDataClasses.add(sms);
            } while (cursor.moveToNext());
        }
        return smsDataClasses;
    }
}
