package com.businessreviewshub.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.businessreviewshub.R;

/**
 * Created by akshay on 12-12-2016.
 */
public class SendSMSFragment extends BaseFragment {

    private EditText mEdtPhNo;

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
        mEdtPhNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        return view;
    }
}
