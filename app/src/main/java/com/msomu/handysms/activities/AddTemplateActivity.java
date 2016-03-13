package com.msomu.handysms.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msomu.handysms.DatabaseHelper;
import com.msomu.handysms.R;
import com.msomu.handysms.models.ProviderModel;
import com.msomu.handysms.models.SenderModel;
import com.msomu.handysms.models.SmsDataClass;

import java.util.ArrayList;

public class AddTemplateActivity extends AppCompatActivity {

    private static final String TAG = "AddTemplateActivity";

    private static final String KEY_BODY = "KEY_BODY";
    private static final String KEY_ADDRESS = "KEY_ADDRESS";
    private static final String KEY_BUNDLE = "KEY_BUNDLE";

    private String body = "";
    private String addr = "";

    private DatabaseHelper db;
    private ArrayList<ProviderModel> allProviders;
    private AutoCompleteTextView providerAcET;

    public static Intent makeIntent(Activity activity, SmsDataClass smsDataClass) {
        Intent intent = new Intent(activity, AddTemplateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BODY, smsDataClass.getBody());
        bundle.putString(KEY_ADDRESS, smsDataClass.getAddress());
        intent.putExtra(KEY_BUNDLE, bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);
        db = new DatabaseHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras().getBundle(KEY_BUNDLE);
            if (bundle != null) {
                body = bundle.getString(KEY_BODY);
                addr = bundle.getString(KEY_ADDRESS);
            }
        }

        allProviders = db.getAllProviders();
        ArrayList<String> stringNames = new ArrayList<>();
        for (int i = 0; i < allProviders.size(); i++) {
            stringNames.add(allProviders.get(i).getProvider());
        }


        TextView sender = (TextView) findViewById(R.id.sender);
        TextView bodyText = (TextView) findViewById(R.id.text_body);
        providerAcET = (AutoCompleteTextView) findViewById(R.id.provider);
        providerAcET.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringNames));
        final Button add = (Button) findViewById(R.id.add_template);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SenderModel senderModel = new SenderModel();
                long providerId = getProviderId();
                if (providerId != -1) {
                    senderModel.setSender(addr);
                    long senderId = db.createSender(senderModel);
                    if (senderId != -1) {
                        long l = db.mapSenderProvider(senderId, providerId);
                    }
                }
                Toast.makeText(AddTemplateActivity.this, "New Template for " + providerAcET, Toast.LENGTH_SHORT).show();
                db.close();
                finish();
            }
        });
        sender.setText(addr);
        bodyText.setText(body);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private long getProviderId() {
        for (int i = 0; i < allProviders.size(); i++) {
            if (providerAcET.getText().toString().equals(allProviders.get(i).getProvider())) {
                return allProviders.get(i).getId();
            }
        }
        ProviderModel providerModel = new ProviderModel();
        providerModel.setProvider(providerAcET.getText().toString());
        return db.createProvider(providerModel);
    }
}
