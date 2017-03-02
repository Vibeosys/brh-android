package com.businessreviewshub.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.businessreviewshub.R;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.requestDataDTO.ForgotPasswordDTO;
import com.businessreviewshub.data.requestDataDTO.LoginRequestDTO;
import com.businessreviewshub.utils.NetworkUtils;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.google.gson.Gson;

public class ForgotPassword extends BaseActivity implements View.OnClickListener, ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {
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
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
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
                        callToLoginWebService();
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

    private void callToLoginWebService() {
        String mUserNameStr = mUserEmailId.getText().toString().trim();
        String mUserCompany = mCompanyCode.getText().toString().trim();
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO(mUserCompany, mUserNameStr);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(forgotPasswordDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_FORGOT_PASSWORD,
                mSessionManager.getForgotPasswordUrl(), baseRequestDTO);

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getResources().getString(R.string.str_err_server_err), error.getMessage());

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_FORGOT_PASSWORD:
                customAlterDialog(getResources().getString(R.string.str_err_server_err), errorMessage);

        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_FORGOT_PASSWORD:
                customAlterDialog(getResources().getString(R.string.title_activity_forgot_password)
                        , getResources().getString(R.string.forgot_pwd_message));
        }

    }
}
