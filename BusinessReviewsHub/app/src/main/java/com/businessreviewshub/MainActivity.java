package com.businessreviewshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.businessreviewshub.fragments.EditProfileFragment;
import com.businessreviewshub.fragments.HistoryDetailsFragment;
import com.businessreviewshub.fragments.SendSMSFragment;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mSendSmsLay, mHistoryLay, mProfileLay;
    private static final String SMS_FRAGMENT = "sms";
    private static final String HISTORY_FRAGMENT = "history";
    private static final String PROFILE_FRAGMENT = "profile";
    private TextView mSendSmsTv, mHistoryTv, mProfileTv;
    private ImageView mSendSmsImg, mHistoryImg, mProfileImg;

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

        mSendSmsTv = (TextView) findViewById(R.id.txtSendSms);
        mHistoryTv = (TextView) findViewById(R.id.historyTxt);
        mProfileTv = (TextView) findViewById(R.id.profileTxt);

        mSendSmsImg = (ImageView) findViewById(R.id.sendSmsImg);
        mHistoryImg = (ImageView) findViewById(R.id.historyImg);
        mProfileImg = (ImageView) findViewById(R.id.profileImg);


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
                mSendSmsTv.setTextColor(getResources().getColor(R.color.colorBottom_yellow));
                mHistoryTv.setTextColor(getResources().getColor(android.R.color.white));
                mProfileTv.setTextColor(getResources().getColor(android.R.color.white));
                mSendSmsImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_sms_yellow_24));
                mHistoryImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_timer_white_24));
                mProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_white_24));
                break;
            case R.id.historyLay:
                HistoryDetailsFragment historyDetailsFragment = new HistoryDetailsFragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_frame_lay, historyDetailsFragment, HISTORY_FRAGMENT).commit();
                mSendSmsTv.setTextColor(getResources().getColor(android.R.color.white));
                mHistoryTv.setTextColor(getResources().getColor(R.color.colorBottom_yellow));
                mProfileTv.setTextColor(getResources().getColor(android.R.color.white));
                mSendSmsImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_textsms_white_24dp));
                mHistoryImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_timer_yellow_24));
                mProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_white_24));
                break;
            case R.id.profileLay:
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_frame_lay, editProfileFragment, PROFILE_FRAGMENT).commit();
                mSendSmsTv.setTextColor(getResources().getColor(android.R.color.white));
                mHistoryTv.setTextColor(getResources().getColor(android.R.color.white));
                mProfileTv.setTextColor(getResources().getColor(R.color.colorBottom_yellow));
                mSendSmsImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_textsms_white_24dp));
                mHistoryImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_timer_white_24));
                mProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_yellow_24));
                break;
            default:
                mSendSmsTv.setTextColor(getResources().getColor(R.color.colorBottom_yellow));
                mHistoryTv.setTextColor(getResources().getColor(android.R.color.white));
                mProfileTv.setTextColor(getResources().getColor(android.R.color.white));
                mSendSmsImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_sms_yellow_24));
                mHistoryImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_timer_white_24));
                mProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_white_24));
                break;
        }
    }
}
