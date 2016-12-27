package com.businessreviewshub.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.businessreviewshub.MainActivity;
import com.businessreviewshub.R;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.requestDataDTO.LoginRequestDTO;
import com.businessreviewshub.data.responseDataDTO.LoginResponseDTO;
import com.businessreviewshub.data.responseDataDTO.UserDTO;
import com.businessreviewshub.utils.DialogUtils;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.businessreviewshub.utils.UserAuth;
import com.google.gson.Gson;

public class LoginActivity extends BaseActivity implements View.OnClickListener, ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    private Button mBtnLogin;
    private EditText mEdtCmpName, mEditPassword, mEditCompanyCode, mUserName;
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
        mEditPassword = (EditText) findViewById(R.id.et_password);
        mEditCompanyCode = (EditText) findViewById(R.id.et_select_cmp);
        mUserName = (EditText) findViewById(R.id.et_username);
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
        }, 43, 69, 0);
        mTxtWebsite.setText(ssWebsite, TextView.BufferType.SPANNABLE);
        mBtnLogin.setOnClickListener(this);
        mEdtCmpName.setOnClickListener(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                boolean returnVal = callToValidation();
                if (returnVal == true) {
                    progressDialog.show();
                    callToLoginWebService();
                }
               /* startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();*/
                break;
            /*case R.id.et_select_cmp:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, mEdtCmpName, mEdtCmpName.getTransitionName());
                    startActivityForResult(new Intent(this, SearchCompanyActivity.class), 1, options.toBundle());

                } else {
                    startActivityForResult(new Intent(getApplicationContext(), SearchCompanyActivity.class), 1);
                }
                break;*/
        }
    }


    private boolean callToValidation() {
        String mUserNameStr = mUserName.getText().toString().trim();
        String mUserPw = mEditPassword.getText().toString().trim();
        String mUserCompany = mEditCompanyCode.getText().toString().trim();
        if (TextUtils.isEmpty(mUserNameStr)) {
            mUserName.requestFocus();
            mUserName.setError("Please Enter User Name");
            return false;
        } else if (TextUtils.isEmpty(mUserPw)) {
            mEditPassword.requestFocus();
            mEditPassword.setError("Please Enter User password Name");
            return false;
        } else if (TextUtils.isEmpty(mUserCompany)) {
            mEditCompanyCode.requestFocus();
            mEditCompanyCode.setError("Please Enter User Name");
            return false;
        }
        return true;
    }

    private void callToLoginWebService() {
        String mUserNameStr = mUserName.getText().toString().trim();
        String mUserPw = mEditPassword.getText().toString().trim();
        String mUserCompany = mEditCompanyCode.getText().toString().trim();
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(mUserNameStr, mUserPw, mUserCompany);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(loginRequestDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_LOGIN,
                mSessionManager.getLogInUrl(), baseRequestDTO);

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
            case ServerRequestConstants.REQUEST_LOGIN:
                customAlterDialog(getResources().getString(R.string.str_err_server_err), errorMessage);

        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_LOGIN:
                LoginResponseDTO loginResponseDTO = LoginResponseDTO.deserializeJson(data);
                String empCodeStr = loginResponseDTO.getGetEmpCode();
                String empNameStr = loginResponseDTO.getEmpName();
                String empPhoneStr = loginResponseDTO.getPhoneNo();
                UserDTO userDTO = new UserDTO();
                userDTO.setEmpCode(empCodeStr);
                userDTO.setEmpName(empNameStr);
                userDTO.setPhoneNo(empPhoneStr);
                userDTO.setEmpPwd(mEditPassword.getText().toString().trim());
                userDTO.setCompanyCode(mEditCompanyCode.getText().toString().trim());
                UserAuth userAuth = new UserAuth();
                userAuth.saveAuthenticationInfo(userDTO, getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }
}
