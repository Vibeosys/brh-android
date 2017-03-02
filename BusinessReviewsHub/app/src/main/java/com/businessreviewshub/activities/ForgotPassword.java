package com.businessreviewshub.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.businessreviewshub.R;
import com.businessreviewshub.utils.NetworkUtils;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {
    private Button mForgotPassword;
    private EditText mCompanyCode, mUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.title_activity_forgot_password));
        setContentView(R.layout.activity_forgot_password);
        mForgotPassword = (Button) findViewById(R.id.btn_forgot_pw);
        mCompanyCode = (EditText) findViewById(R.id.et_select_fp);
        mUserEmailId = (EditText) findViewById(R.id.et_username_fp);
        mForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_forgot_pw:
                boolean returnVal = callToValidation();
                if (returnVal == true) {
                    if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                        progressDialog.show();
                        // callToLoginWebService();
                    } else if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                        createAlertDialog(getResources().getString(R.string.app_name), getResources().getString(R.string.str_no_internet));
                    }

                }
                break;
        }
    }

    private boolean callToValidation() {
        String mUserNameStr = mUserEmailId.getText().toString().trim();
        String mUserCompany = mCompanyCode.getText().toString().trim();
        if (TextUtils.isEmpty(mUserCompany)) {
            mCompanyCode.requestFocus();
            mCompanyCode.setError(getResources().getString(R.string.str_company_code));
            return false;
        } else if (TextUtils.isEmpty(mUserNameStr)) {
            mUserEmailId.requestFocus();
            mUserEmailId.setError(getResources().getString(R.string.str_user_name));
            return false;
        } else if (!TextUtils.isEmpty(mUserNameStr)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(mUserEmailId.getText().toString()).matches()) {
                mUserEmailId.requestFocus();
                mUserEmailId.setError(getResources().getString(R.string.str_user_invalid));
                return false;
            }
        }
        return true;
    }
}
