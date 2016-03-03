package com.msomu.handysms;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String transationalAddressPattern = "(?!^\\d+$)^(?!^\\+)^(?!^\\d+\\t*\\d)^.+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parseSms(readAllMessage());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void parseSms(List<SmsDataClass> smsDataClasses) {
        for (int i = 0; i < smsDataClasses.size(); i++) {
            boolean smsTrasactional = findTransactionalSmsOrNot(smsDataClasses.get(i));
            Log.d("MainActivity", smsDataClasses.get(i).getAddress() + " " + smsTrasactional);
        }
    }

    private boolean findTransactionalSmsOrNot(SmsDataClass smsDataClass) {
        return smsDataClass.getAddress() != null && smsDataClass.getAddress().matches(transationalAddressPattern);
    }

    public List<SmsDataClass> readAllMessage(){
        List<SmsDataClass> sms = new ArrayList<SmsDataClass>();

        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);
        while (cur.moveToNext())
        {
            SmsDataClass smsDataClass = new SmsDataClass();
            smsDataClass.setBody(cur.getString(cur.getColumnIndexOrThrow("body")));
            smsDataClass.setAddress(cur.getString(cur.getColumnIndexOrThrow("address")));
            //sms.add(body);
            //Log.d("Cursor Body",body);
            Log.d(" Sms Data ",smsDataClass.getAddress()+" : "+smsDataClass.getBody());
            sms.add(smsDataClass);
        }
        Log.d("MainActivity","Total sms read"+sms.size());
        cur.close();
        return sms;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
