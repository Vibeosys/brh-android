package com.businessreviewshub.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.businessreviewshub.R;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.requestDataDTO.SendSMSRequestDTO;
import com.businessreviewshub.data.responseDataDTO.UserInfoDTO;
import com.businessreviewshub.utils.Constants;
import com.businessreviewshub.utils.NetworkUtils;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Locale;

/**
 * Created by akshay on 12-12-2016.
 */
public class SendSMSFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {

    private EditText mEdtPhNo, mEditCustomerName;
    private Button mSendSMSBtn, mCancelBtn;
    private TextView mCompanyNameTv;
    private ImageView mCompanyLogo;

    public SendSMSFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Company Name");
        Log.d("TAG", "TAG");
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
        mCompanyNameTv = (TextView) view.findViewById(R.id.sms_txtLogin);
        mCompanyLogo = (ImageView) view.findViewById(R.id.sms_imgLogo);

        mSendSMSBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mEdtPhNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
        //PhoneNumberUtils.formatNumber(mEdtPhNo.getText().toString(), Locale.getDefault().getCountry());
       /* mEdtPhNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());*/
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        String companyName = mSessionManager.getEmployeeCompanyName();
        String companyLogo = mSessionManager.getEmployeeCompanyLogoUrl();
        if (companyName != null)
            mCompanyNameTv.setText("" + companyName);
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.execute(companyLogo);

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
                    if(NetworkUtils.isActiveNetworkAvailable(getContext()))
                    {progressDialog.show();
                        callToWebService();

                    }else if(!NetworkUtils.isActiveNetworkAvailable(getContext()))
                    {
                        createAlertDialog(getResources().getString(R.string.app_name), getResources().getString(R.string.str_no_internet));
                    }

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
        String stripString = PhoneNumberUtils.stripSeparators(mCusterPhoneNumber);
        SendSMSRequestDTO sendSMSRequestDTO = new SendSMSRequestDTO(stripString, mCustomerName);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(sendSMSRequestDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_SEND_SMS,
                mSessionManager.getSendSMS(), baseRequestDTO);


    }

    private boolean callToValidationForSMS() {
        String mCustomerName = mEditCustomerName.getText().toString().trim();
        String mCusterPhoneNumber = mEdtPhNo.getText().toString().trim();
        String stripString = PhoneNumberUtils.stripSeparators(mCusterPhoneNumber);

        if (TextUtils.isEmpty(mCustomerName)) {
            mEditCustomerName.requestFocus();
            mEditCustomerName.setError(getResources().getString(R.string.str_sms_cust));
            return false;
        } else if (TextUtils.isEmpty(mCusterPhoneNumber)) {
            mEdtPhNo.requestFocus();
            mEdtPhNo.setError(getResources().getString(R.string.str_sms_cust_ph));
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

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                mCompanyLogo.setImageBitmap(result);
            }
        }
    }
}
