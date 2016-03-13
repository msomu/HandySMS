package com.msomu.handysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.msomu.handysms.model.ProviderModel;
import com.msomu.handysms.model.SenderModel;
import com.msomu.handysms.model.SmsDataClass;

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
    private static final String PROVIDER_TABLE = "provider_table";
    private static final String SENDER_TABLE = "sender_table";
    private static final String SENDER_PROVIDER_TABLE = "sender_provider_table";
    private static final String SMS_TABLE = "sms_table";

    private static final String ID = "_id";
    private static final String PROVIDER_NAME = "provider_name";
    private static final String SENDER_CODE = "sender_code";
    private static final String SENDER_ID = "sender_id";
    private static final String PROVIDER_ID = "provider_id";
    private static final String SMS = "sms";
    private static final String DATE = "date_time";

    private static final String CREATE_PROVIDER = "CREATE TABLE " + PROVIDER_TABLE + " (" + ID + " INT PRIMARY KEY NOT NULL," + PROVIDER_NAME + " TEXT NOT NULL);";
    private static final String CREATE_SENDER = "CREATE TABLE " + SENDER_TABLE + " (" + ID + " INT PRIMARY KEY NOT NULL," + SENDER_CODE + " TEXT NOT NULL);";
    private static final String CREATE_SENDER_PROVIDER = "CREATE TABLE " + SENDER_PROVIDER_TABLE + " (" + ID + " INT PRIMARY KEY NOT NULL," + SENDER_ID + " INT NOT NULL," + PROVIDER_ID + " INT NOT NULL);";
    private static final String CREATE_SMS = "CREATE TABLE " + SMS_TABLE + " (" + ID + " INT PRIMARY KEY NOT NULL," + SENDER_ID + " TEXT NOT NULL," + SMS + " TEXT NOT NULL, " + DATE + " TIMESTAMP NOT NULL);";
    private static final String TAG = DatabaseHandler.class.getSimpleName();


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVIDER);
        db.execSQL(CREATE_SENDER);
        db.execSQL(CREATE_SENDER_PROVIDER);
        db.execSQL(CREATE_SMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROVIDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SENDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SENDER_PROVIDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SMS_TABLE);
        onCreate(db);
    }

    /*
    * Creating a provider
    */
    public long createProvider(ProviderModel providerModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROVIDER_NAME, providerModel.getProvider());
        // insert row
        long providerId = db.insert(PROVIDER_TABLE, null, values);
        return providerId;
    }

    /*
    * Creating a sender
    */
    public long createSender(SenderModel senderModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SENDER_CODE, senderModel.getSender());
        // insert row
        return db.insert(SENDER_TABLE, null, values);
    }

    /*
   * Map a sender
   */
    public long mapSenderProvider(int senderId, int providerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SENDER_ID, senderId);
        values.put(PROVIDER_ID, providerId);
        // insert row
        return db.insert(SENDER_PROVIDER_TABLE, null, values);
    }

    public long addSMS(SmsDataClass smsDataClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SMS, smsDataClass.getBody());
        values.put(DATE, smsDataClass.getDate());
        //  values.put(SENDER_ID, smsDataClass.getAddress());
        // insert row
        return db.insert(SMS_TABLE, null, values);
    }

    /*
 * get single provider
 */
    public ProviderModel getProvider(long providerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + PROVIDER_TABLE + " WHERE "
                + ID + " = " + providerId;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        ProviderModel providerModel = new ProviderModel();
        providerModel.setId(c.getInt(c.getColumnIndex(ID)));
        providerModel.setProvider((c.getString(c.getColumnIndex(PROVIDER_NAME))));
        c.close();
        return providerModel;
    }

    public SenderModel getSenderCode(long senderId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + SENDER_TABLE + " WHERE "
                + ID + " = " + senderId;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        SenderModel senderModel = new SenderModel();
        senderModel.setId(c.getInt(c.getColumnIndex(ID)));
        senderModel.setSender((c.getString(c.getColumnIndex(SENDER_CODE))));
        c.close();
        return senderModel;
    }

    public SenderModel getSenderId(String senderCode) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + SENDER_TABLE + " WHERE "
                + SENDER_CODE + " = " + senderCode;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        SenderModel senderModel = new SenderModel();
        senderModel.setId(c.getInt(c.getColumnIndex(ID)));
        senderModel.setSender((c.getString(c.getColumnIndex(SENDER_CODE))));
        c.close();
        return senderModel;
    }
}
