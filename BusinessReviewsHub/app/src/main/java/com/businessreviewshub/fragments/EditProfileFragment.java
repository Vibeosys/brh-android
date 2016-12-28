package com.businessreviewshub.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.businessreviewshub.R;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.requestDataDTO.EditProfileRequestDTO;
import com.businessreviewshub.data.requestDataDTO.SendSMSRequestDTO;
import com.businessreviewshub.data.responseDataDTO.UserDTO;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.businessreviewshub.utils.UserAuth;
import com.google.gson.Gson;

public class EditProfileFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {
    private EditText mUserFirstName, mUserPhoneNo, mUserPassword;
    private Button mUpdateSMS;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        // Inflate the layout for this fragment
        mUserFirstName = (EditText) view.findViewById(R.id.firstNameTv);
        mUserPhoneNo = (EditText) view.findViewById(R.id.lastNameTv);
        mUserPassword = (EditText) view.findViewById(R.id.passwordTv);
        mUpdateSMS = (Button) view.findViewById(R.id.updateProfile);
        mUserFirstName.setText("" + mSessionManager.getEmployeeName());
        mUserPhoneNo.setText("" + mSessionManager.getEmployeePhone());
        mUserPassword.setText("" + mSessionManager.getEmployeePassword());

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mUpdateSMS.setOnClickListener(this);
        mUserFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!b) {
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
                }
            }
        });
        mUserPhoneNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    mUserPhoneNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else if (!b)
                    mUserPhoneNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
            }
        });
        mUserPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    mUserPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else if (!b)
                    mUserPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
            }
        });
        FloatingActionButton mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_profile);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast toast = Toast.makeText(getActivity(), "User information is updated successfully ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();*/

            }
        });
        return view;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.updateProfile:
                boolean returnVal = callToValidation();
                if (returnVal == true) {
                    callToWebService();
                }
                break;

        }
    }

    private void callToWebService() {
        String mUserName = mUserFirstName.getText().toString().trim();
        String mUserPhone = mUserPhoneNo.getText().toString().trim();
        String mUserPwd = mUserPassword.getText().toString().trim();
        EditProfileRequestDTO editProfileRequestDTO = new EditProfileRequestDTO(mUserName, mUserPhone, mUserPwd);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(editProfileRequestDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_EDIT_PROFILE,
                mSessionManager.getEditProfileUrl(), baseRequestDTO);

        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
    }

    private boolean callToValidation() {
        String mUserName = mUserFirstName.getText().toString().trim();
        String mUserPhone = mUserPhoneNo.getText().toString().trim();
        String mUserPwd = mUserPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName)) {
            mUserFirstName.setError("Please enter your name");
            return false;
        }
        if (TextUtils.isEmpty(mUserPhone)) {
            mUserPhoneNo.setError("Please enter your phone number");
            return false;
        }
        if (TextUtils.isEmpty(mUserPwd)) {
            mUserPassword.setError("Please enter your password");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        createAlertDialog(getResources().getString(R.string.str_err_server_err), error.getMessage());
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        createAlertDialog(getResources().getString(R.string.str_err_server_err), errorMessage);

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        createAlertDialog(getResources().getString(R.string.str_prfile), getResources().getString(R.string.str_prfile_success));
        String mUserName = mUserFirstName.getText().toString().trim();
        String mUserPhone = mUserPhoneNo.getText().toString().trim();
        String mUserPwd = mUserPassword.getText().toString().trim();

        UserDTO userDTO = new UserDTO();
        userDTO.setEmpCode(mSessionManager.getCompanyCode());
        userDTO.setEmpName(mUserName);
        userDTO.setPhoneNo(mUserPhone);
        userDTO.setEmpPwd(mUserPassword.getText().toString().trim());
        userDTO.setCompanyCode(mSessionManager.getEmployeeCode());
        UserAuth userAuth = new UserAuth();
        userAuth.saveAuthenticationInfo(userDTO, getContext().getApplicationContext());

    }
}
