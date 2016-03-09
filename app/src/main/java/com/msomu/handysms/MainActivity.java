package com.msomu.handysms;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String transationalAddressPattern = "(?!^\\d+$)^(?!^\\+)^(?!^\\d+\\t*\\d)^.+$";
    private int totalSms = 0;
    private int transactionalSms = 0;
    private ProgressBar readingSmsProgressBar;
    private LinearLayout loadingReadSms;


    private ArrayList<String> templateArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        readingSmsProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        loadingReadSms = (LinearLayout) findViewById(R.id.loading);

        new ReadSmsAndAnalyse().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void fragmentShow() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, MainActivityFragment.newInstance(totalSms, transactionalSms));
        fragmentTransaction.commit();
    }

    private void parseSms(List<SmsDataClass> smsDataClasses) {
        totalSms = smsDataClasses.size();
        for (int i = 0; i < smsDataClasses.size(); i++) {
            boolean smsTrasactional = findTransactionalSmsOrNot(smsDataClasses.get(i));
            Log.d("MainActivity", smsDataClasses.get(i).getAddress() + " " + smsTrasactional);
            if (smsTrasactional) {
                transactionalSms++;

            }
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

    private class ReadSmsAndAnalyse extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingReadSms.setVisibility(View.VISIBLE);
            readingSmsProgressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            parseSms(readAllMessage());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadingReadSms.setVisibility(View.GONE);
            fragmentShow();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            readingSmsProgressBar.setProgress(values[0]);
        }
    }
}
