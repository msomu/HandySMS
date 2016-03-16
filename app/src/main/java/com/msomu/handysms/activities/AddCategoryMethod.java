package com.msomu.handysms.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msomu.handysms.DatabaseHelper;
import com.msomu.handysms.R;
import com.msomu.handysms.models.CategoriesModel;

import java.util.ArrayList;

public class AddCategoryMethod extends AppCompatActivity {


    private static final String KEY_BODY = "body";
    private static final String KEY_PROVIDER_ID = "provider";
    private static final String KEY_SMS_ID = "smsId";
    private static final String KEY_BUNDLE = "bundle";
    private static final String KEY_PROVIDER_NAME = "provider_name";
    private static final String TAG = "AddCategoryMethod";

    private String body = "";
    private String provider = "";
    private int providerId;
    private int smsId;
    private DatabaseHelper db;

    private ArrayList<CategoriesModel> allCAtegories;
    private AutoCompleteTextView categoryAutocomplete;

    public static Intent makeIntent(Activity activity, String body, int providerID, String providerName, int smsId) {
        Intent intent = new Intent(activity, AddCategoryMethod.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BODY, body);
        bundle.putInt(KEY_PROVIDER_ID, providerID);
        bundle.putInt(KEY_SMS_ID, smsId);
        bundle.putString(KEY_PROVIDER_NAME, providerName);
        intent.putExtra(KEY_BUNDLE, bundle);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_method);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHelper(getApplicationContext());
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras().getBundle(KEY_BUNDLE);
            if (bundle != null) {
                body = bundle.getString(KEY_BODY);
                provider = bundle.getString(KEY_PROVIDER_NAME);
                providerId = bundle.getInt(KEY_PROVIDER_ID);
                smsId = bundle.getInt(KEY_SMS_ID);
                Log.d(TAG, body + providerId + provider);
            }
        }
        allCAtegories = db.getCatgoriesofProvider(providerId);
        ArrayList<String> stringNames = new ArrayList<>();
        for (int i = 0; i < allCAtegories.size(); i++) {
            stringNames.add(allCAtegories.get(i).getCategoryName());
            Log.d(TAG, stringNames.get(i));
        }

        TextView sender = (TextView) findViewById(R.id.provider_text);
        TextView bodyText = (TextView) findViewById(R.id.text_body);
        categoryAutocomplete = (AutoCompleteTextView) findViewById(R.id.categoryAutocomplete);
        categoryAutocomplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringNames));
        final Button add = (Button) findViewById(R.id.add_category);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long categoryId = getCategoryId();
                if (categoryId != -1) {
                    long l = db.mapCategoriesProvider(categoryId, providerId);
                    db.updateSms(categoryId, smsId);
                }
                Toast.makeText(AddCategoryMethod.this, "New Template for " + categoryAutocomplete, Toast.LENGTH_SHORT).show();
                db.close();
                finish();
            }
        });
        sender.setText(provider);
        bodyText.setText(body);

    }

    private long getCategoryId() {
        for (int i = 0; i < allCAtegories.size(); i++) {
            if (categoryAutocomplete.getText().toString().equals(allCAtegories.get(i).getCategoryName())) {
                return allCAtegories.get(i).getId();
            }
        }
        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.setCategoryName(categoryAutocomplete.getText().toString());
        return db.createCategory(categoriesModel);
    }

}
