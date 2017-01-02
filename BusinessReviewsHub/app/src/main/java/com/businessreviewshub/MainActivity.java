package com.businessreviewshub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.businessreviewshub.activities.BaseActivity;
import com.businessreviewshub.activities.LoginActivity;
import com.businessreviewshub.data.responseDataDTO.UserInfoDTO;
import com.businessreviewshub.fragments.EditProfileFragment;
import com.businessreviewshub.fragments.HistoryDetailsFragment;
import com.businessreviewshub.fragments.SendSMSFragment;
import com.businessreviewshub.utils.Constants;
import com.businessreviewshub.utils.UserAuth;
import com.yalantis.ucrop.UCrop;

import org.w3c.dom.Text;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mSendSmsLay, mHistoryLay, mProfileLay;
    private static final String SMS_FRAGMENT = "sms";
    private static final String HISTORY_FRAGMENT = "history";
    private static final String PROFILE_FRAGMENT = "profile";
    private TextView mSendSmsTv, mHistoryTv, mProfileTv;
    private ImageView mSendSmsImg, mHistoryImg, mProfileImg;
    private DataReceived dataReceived;

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
        if (!UserAuth.isUserLoggedIn()) {
            // finish();
            callLogin();
            return;
        }
        setUpFragment(R.id.sendSmsLay);
    }

    private void callLogin() {

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                dataReceived.onDataReceived(data);

            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            dataReceived.onErrorReceived(data);
        }

    }

    public interface DataReceived {
        public void onDataReceived(Intent data);

        public void onErrorReceived(Intent data);
    }

    public void setDataReceived(DataReceived listener) {
        dataReceived = listener;
    }
}
