package com.businessreviewshub.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.businessreviewshub.MainActivity;
import com.businessreviewshub.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogin;
    private EditText mEdtCmpName;
    private Context mContext = this;
    private TextView mTxtCopyright, mTxtWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_new);
        setTitle(getResources().getString(R.string.str_login_activity));
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mEdtCmpName = (EditText) findViewById(R.id.et_select_cmp);
        mTxtCopyright = (TextView) findViewById(R.id.txtCopyRight);
        mTxtWebsite = (TextView) findViewById(R.id.txtWebsite);
        mTxtWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder ssWebsite = new SpannableStringBuilder(getString(R.string.str_login_website));
        ssWebsite.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vibeosys.com/"));
                startActivity(browserIntent);
            }
        }, 43, 59, 0);
        mTxtWebsite.setText(ssWebsite, TextView.BufferType.SPANNABLE);
        mBtnLogin.setOnClickListener(this);
        mEdtCmpName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.et_select_cmp:
                openDialog();
                break;
        }
    }

    private void openDialog() {
        Dialog dialog = new Dialog(mContext, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_select_company);
        dialog.show();
    }
}
