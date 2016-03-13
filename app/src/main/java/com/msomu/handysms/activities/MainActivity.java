package com.msomu.handysms.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msomu.handysms.DatabaseHelper;
import com.msomu.handysms.R;
import com.msomu.handysms.adapters.CategorisedSMSAdapter;
import com.msomu.handysms.adapters.SmsViewAdapter;
import com.msomu.handysms.models.ProviderModel;
import com.msomu.handysms.models.SmsDataClass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements SmsViewAdapter.OnItemClickListener, CategorisedSMSAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static List<ProviderModel> items = new ArrayList<>();
    private ArrayList<SmsDataClass> transactionalSmses;

    private int totalSms = 0;
    private int transactionalSms = 0;
    private ProgressBar readingSmsProgressBar;
    private LinearLayout loadingReadSms;
    private LinearLayout data;
    private ProgressBar smsProgressBar;
    private TextView transactionalSMSCount;
    private TextView normalSMSCount;
    private SmsViewAdapter smsViewAdapter;
    private CategorisedSMSAdapter categorisedSMSAdapter;
    private DatabaseHelper db;

    private Pattern patternRs = Pattern.compile("(Rs ?.? ?\\d+\\.?(\\d{0,2})?\\.?)");
    //Pattern patternDebited = Pattern.compile("Debited");
    //Pattern patternWithdrawn = Pattern.compile("withdrawn");
    //Pattern patternTxn = Pattern.compile("txn");
    //Pattern patternCredited = Pattern.compile("Debited");


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        transactionalSmses = new ArrayList<>();
        readingSmsProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        loadingReadSms = (LinearLayout) findViewById(R.id.loading);
        data = (LinearLayout) findViewById(R.id.data);

        smsProgressBar = (ProgressBar) findViewById(R.id.smsProgrssBar);
        transactionalSMSCount = (TextView) findViewById(R.id.transactionalSMSCount);
        normalSMSCount = (TextView) findViewById(R.id.normalSMSCount);

        initRecyclerView();
        db = new DatabaseHelper(getApplicationContext());
        refresh();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Updating Sms", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                db.clearDB();
                new ReadSmsAndAnalyse().execute();
            }
        });
    }

    /**
     * Used to refresh the view
     */
    private void refresh() {
        items.clear();
        items.addAll(db.getAllProviders());
        categorisedSMSAdapter.notifyDataSetChanged();
    }

    /**
     * Initiataion of RecyclerViews
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView categorisedSmsRecyclerView = (RecyclerView) findViewById(R.id.categorisedSms);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //SMSProviderViewAdapter adapter = new SMSProviderViewAdapter(items);
        // adapter.setOnItemClickListener(this);
        categorisedSmsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categorisedSMSAdapter = new CategorisedSMSAdapter(items);
        categorisedSmsRecyclerView.setAdapter(categorisedSMSAdapter);
        categorisedSMSAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        smsViewAdapter = new SmsViewAdapter(transactionalSmses);
        smsViewAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(smsViewAdapter);
    }


    /**
     *
     * @param smsDataClasses is the sms's read from the Inbox
     */
    private void parseSms(List<SmsDataClass> smsDataClasses) {
        totalSms = smsDataClasses.size();
        for (int i = 0; i < smsDataClasses.size(); i++) {
            boolean smsTrasactional = findTransactionalSmsOrNot(smsDataClasses.get(i));
            Log.d("MainActivity", smsDataClasses.get(i).getAddress() + " " + smsTrasactional);
            if (smsTrasactional) {
                if (checkForRs(smsDataClasses.get(i).getBody())) {
                    transactionalSms++;
                    long senderID = db.getSenderID(smsDataClasses.get(i).getAddress());
                    if (senderID == -1) {
                        Log.d(TAG, "Added " + senderID);
                        transactionalSmses.add(smsDataClasses.get(i));
                    } else {
                        Log.d(TAG, "Skiped " + senderID);
                        db.createSMS(smsDataClasses.get(i).getBody(), senderID, smsDataClasses.get(i).getDate());
                    }
                }
            }
        }
    }

    /**
     *
     * @param body Is the message of the sms Checking for Rs . String in it
     *             Had Idea of checking scenarios like debited, credited, purchased
     * @return
     */
    private boolean checkForRs(String body) {
        Log.d(TAG, "Trying to match : " + body);
        Matcher matcher = patternRs.matcher(body);
        if (matcher.find()) {
            Log.d(TAG, "Matched " + matcher.group(1));
//            matcher = patternDebited.matcher(body);
//            if (matcher.find()) {
//                Log.d(TAG, "Matched Debited");
            return true;
//            }else{
//                return false;
//            }
        } else {
            Log.d(TAG, "No Match");
            return false;
        }
    }

    /**
     *
     * @param smsDataClass The basic identification is it a personal sms or non personal SMS
     * @return
     */
    private boolean findTransactionalSmsOrNot(SmsDataClass smsDataClass) {
        return smsDataClass.getAddress() != null && smsDataClass.getAddress().matches("(?!^\\d+$)^(?!^\\+)^(?!^\\d+\\t*\\d)^.+$");
    }

    /**
     *
     * @return
     */
    public List<SmsDataClass> readAllMessage() {
        ArrayList<SmsDataClass> sms = new ArrayList<SmsDataClass>();

        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);
        if (cur != null) {
            while (cur.moveToNext()) {
                SmsDataClass smsDataClass = new SmsDataClass();
                smsDataClass.setBody(cur.getString(cur.getColumnIndexOrThrow("body")));
                smsDataClass.setAddress(cur.getString(cur.getColumnIndexOrThrow("address")));
                smsDataClass.setDate(cur.getString(cur.getColumnIndexOrThrow("date")));
                //sms.add(body);
                //Log.d("Cursor Body",body);
                Log.d(" Sms Data ", smsDataClass.getAddress() + " : " + smsDataClass.getBody());
                sms.add(smsDataClass);
            }
            cur.close();
        }
//        ProviderModel providerModel = new ProviderModel();
//        providerModel.setSmsDataClassArrayList(sms);
//        providerModel.setId(0);
//        providerModel.setProvider("IOB");
        // items.add(providerModel);
        Log.d("MainActivity", "Total sms read" + sms.size());
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

    @Override
    public void onItemClick(View view, SmsDataClass viewModel) {
        startActivity(AddTemplateActivity.makeIntent(this, viewModel));
    }

    @Override
    public void onItemClick(View view, ProviderModel providerModel) {
        startActivity(TxSmsListActivity.makeIntent(this, providerModel.getId()));
    }

    private class ReadSmsAndAnalyse extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingReadSms.setVisibility(View.VISIBLE);
            data.setVisibility(View.GONE);
            totalSms = 0;
            transactionalSms = 0;
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
            data.setVisibility(View.VISIBLE);
            transactionalSMSCount.setText("Transactional SMS : " + transactionalSms);
            int normalSms = totalSms - transactionalSms;
            normalSMSCount.setText("Personal SMS : " + normalSms);
            smsProgressBar.setMax(totalSms);
            smsProgressBar.setProgress(transactionalSms);
            smsViewAdapter.notifyDataSetChanged();
            refresh();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            readingSmsProgressBar.setProgress(values[0]);
        }
    }
}
