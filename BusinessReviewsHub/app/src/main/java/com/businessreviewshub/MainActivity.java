package com.businessreviewshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.businessreviewshub.fragments.EditProfileFragment;
import com.businessreviewshub.fragments.HistoryDetailsFragment;
import com.businessreviewshub.fragments.SendSMSFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mSendSmsLay, mHistoryLay, mProfileLay;
    private static final String SMS_FRAGMENT = "sms";
    private static final String HISTORY_FRAGMENT = "history";
    private static final String PROFILE_FRAGMENT = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mSendSmsLay = (LinearLayout) findViewById(R.id.sendSmsLay);
        mHistoryLay = (LinearLayout) findViewById(R.id.historyLay);
        mProfileLay = (LinearLayout) findViewById(R.id.profileLay);

        mSendSmsLay.setOnClickListener(this);
        mHistoryLay.setOnClickListener(this);
        mProfileLay.setOnClickListener(this);
        setUpFragment(R.id.sendSmsLay);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        setUpFragment(id);
    }

    private void setUpFragment(int layoutId) {
        switch (layoutId) {
            case R.id.sendSmsLay:
                SendSMSFragment sendSMSFragment = new SendSMSFragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_frame_lay, sendSMSFragment, SMS_FRAGMENT).commit();
                break;
            case R.id.historyLay:
                HistoryDetailsFragment historyDetailsFragment = new HistoryDetailsFragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_frame_lay, historyDetailsFragment, HISTORY_FRAGMENT).commit();
                break;
            case R.id.profileLay:
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_frame_lay, editProfileFragment, PROFILE_FRAGMENT).commit();
                break;
            default:

                break;
        }
    }
}
