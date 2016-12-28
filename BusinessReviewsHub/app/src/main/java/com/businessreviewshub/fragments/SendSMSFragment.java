package com.businessreviewshub.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.businessreviewshub.R;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.requestDataDTO.SendSMSRequestDTO;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.google.gson.Gson;

/**
 * Created by akshay on 12-12-2016.
 */
public class SendSMSFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {

    private EditText mEdtPhNo, mEditCustomerName;
    private Button mSendSMSBtn, mCancelBtn;

    public SendSMSFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Company Name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_sms, container, false);
        mEdtPhNo = (EditText) view.findViewById(R.id.et_ph_no);
        mEditCustomerName = (EditText) view.findViewById(R.id.et_customer_name);
        mSendSMSBtn = (Button) view.findViewById(R.id.btn_send_sms);
        mCancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        mSendSMSBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
       /* mEdtPhNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());*/
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        return view;
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.cancel();
        createAlertDialog(getResources().getString(R.string.str_err_server_err), error.getMessage());
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.cancel();
        createAlertDialog(getResources().getString(R.string.str_err_server_err), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.cancel();
        createAlertDialog(getResources().getString(R.string.str_sms_success), getResources().getString(R.string.str_sms_Message));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_send_sms:
                boolean returnVal = callToValidationForSMS();
                if (returnVal == true) {
                    progressDialog.show();
                    callToWebService();
                }
                break;
            case R.id.btn_cancel:
                mEdtPhNo.getText().clear();
                mEditCustomerName.getText().clear();
                mEdtPhNo.clearFocus();
                mEditCustomerName.requestFocus();
                //Toast.makeText(getActivity().getApplicationContext(), "Cancel Btn", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void callToWebService() {
        String mCustomerName = mEditCustomerName.getText().toString().trim();
        String mCusterPhoneNumber = mEdtPhNo.getText().toString().trim();
        SendSMSRequestDTO sendSMSRequestDTO = new SendSMSRequestDTO(mCusterPhoneNumber, mCustomerName);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(sendSMSRequestDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_SEND_SMS,
                mSessionManager.getSendSMS(), baseRequestDTO);

        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");
        Log.d("TAG", "TAG");

    }

    private boolean callToValidationForSMS() {
        String mCustomerName = mEditCustomerName.getText().toString().trim();
        String mCusterPhoneNumber = mEdtPhNo.getText().toString().trim();

        if (TextUtils.isEmpty(mCustomerName)) {
            mEditCustomerName.requestFocus();
            mEditCustomerName.setError("Please Enter Customer Name");
            return false;
        } else if (TextUtils.isEmpty(mCusterPhoneNumber)) {
            mEdtPhNo.requestFocus();
            mEdtPhNo.setError("Please Enter Customer Name");
            return false;
        }
        return true;
    }

    protected void createAlertDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEdtPhNo.getText().clear();
                        mEditCustomerName.getText().clear();
                        mEdtPhNo.clearFocus();
                        mEditCustomerName.requestFocus();
                    }
                }).create().show();
    }
}
