package com.msomu.handysms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.msomu.handysms.model.SmsDataClass;

import java.util.ArrayList;
import java.util.List;

public class TxSmsListActivity extends AppCompatActivity implements SmsViewAdapter.OnItemClickListener {

    private static final String KEY_PROVIDER_SMS = "KEY_PROVIDER_SMS";
    private static final String EXTRAS = "KEY_EXTRAS";
    private List<SmsDataClass> smsDataClasses;
    private int providerId;
    private DatabaseHelper db;

    public static Intent makeIntent(Activity activity, int provideId) {
        Intent intent = new Intent(activity, TxSmsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PROVIDER_SMS, provideId);
        intent.putExtra(EXTRAS, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_sms_list);
        db = new DatabaseHelper(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        smsDataClasses = new ArrayList<>();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras().getBundle(EXTRAS);
            if (bundle != null) {
                providerId = bundle.getInt(KEY_PROVIDER_SMS);
                ArrayList<SmsDataClass> allSmsOfProvider = db.getAllSmsOfProvider(providerId);
                smsDataClasses.clear();
                smsDataClasses.addAll(allSmsOfProvider);
                initRecyclerView();
            }
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_sms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SmsViewAdapter adapter = new SmsViewAdapter(smsDataClasses);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View view, SmsDataClass viewModel) {

    }
}
