package com.businessreviewshub.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.businessreviewshub.R;
import com.businessreviewshub.adapter.SmsHistoryAdaptor;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.responseDataDTO.SmsHistoryResponseDTO;
import com.businessreviewshub.utils.DialogUtils;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;

import java.util.ArrayList;

public class HistoryDetailsFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    private ArrayList<SmsHistoryResponseDTO> smsHistoryResponseDTOs;
    protected SmsHistoryAdaptor smsHistoryAdaptor;
    private ListView mHistoryList;

    public HistoryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("History");
        smsHistoryResponseDTOs = new ArrayList<>();
        progressDialog = DialogUtils.getProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        callToWebServiceForHistory();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

    }

    private void callToWebServiceForHistory() {
        progressDialog.show();
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_SMS_HISTORY,
                mSessionManager.getSmsHistoryUrl(), baseRequestDTO);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_details, container, false);
        mHistoryList = (ListView) view.findViewById(R.id.historyList);

        smsHistoryAdaptor = new SmsHistoryAdaptor(smsHistoryResponseDTOs, getActivity().getApplicationContext());
        mHistoryList.setAdapter(smsHistoryAdaptor);
        // smsHistoryAdaptor.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.cancel();
        createAlertDialog(getResources().getString(R.string.str_err_server_err), error.getMessage().toString());


    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.cancel();
        createAlertDialog(getResources().getString(R.string.str_err_server_err), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

        progressDialog.cancel();
        smsHistoryResponseDTOs = SmsHistoryResponseDTO.deserializeToArray(data);
        smsHistoryAdaptor.addItems(smsHistoryResponseDTOs);
    }
}
