package com.msomu.handysms;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TOTAL_SMS = "TOTAL_SMS";
    private static final String TRANSACTIONAL_SMS = "TRANSACTIONAL_SMS";
    private int totalSms;
    private int transactionalSms;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance(int totalSms, int transactionalSm) {
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TOTAL_SMS, totalSms);
        bundle.putInt(TRANSACTIONAL_SMS, transactionalSm);
        mainActivityFragment.setArguments(bundle);
        return mainActivityFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        totalSms = getArguments().getInt(TOTAL_SMS);
        transactionalSms = getArguments().getInt(TRANSACTIONAL_SMS);
        ProgressBar smsProgrssBar = (ProgressBar) rootView.findViewById(R.id.smsProgrssBar);
        TextView transactionalSMSCount = (TextView) rootView.findViewById(R.id.transactionalSMSCount);
        TextView normalSMSCount = (TextView) rootView.findViewById(R.id.normalSMSCount);
        transactionalSMSCount.setText("Transactional SMS : " + transactionalSms);
        int normalSms = totalSms - transactionalSms;
        normalSMSCount.setText("Personal SMS : " + normalSms);
        smsProgrssBar.setMax(totalSms);
        smsProgrssBar.setProgress(transactionalSms);
        return rootView;
    }
}
