package com.businessreviewshub.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.businessreviewshub.R;

public class EditProfileFragment extends BaseFragment {
    private EditText mUserFirstName, mUserLastName, mUserPassword;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_action_bar, null);
        actionBar.setCustomView(v);
        setHasOptionsMenu(true);*/
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        // Inflate the layout for this fragment
        mUserFirstName = (EditText) view.findViewById(R.id.firstNameTv);
        mUserLastName = (EditText) view.findViewById(R.id.lastNameTv);
        mUserPassword = (EditText) view.findViewById(R.id.passwordTv);
        mUserFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    Log.d("TAG","TAG");
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    Log.d("TAG","TAG");
                    Log.d("TAG","TAG");
                }
                else if(!b)
                {
                    mUserFirstName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);

                }
            }
        });
        mUserLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                mUserLastName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else if(!b)
                    mUserLastName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
            }
        });
        mUserPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                mUserPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else if(!b)
                    mUserPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_black_24dp, 0);
            }
        });
        FloatingActionButton mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_profile);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity(), "User information is updated successfully ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        return view;
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

}
