package com.businessreviewshub.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.businessreviewshub.MainActivity;
import com.businessreviewshub.R;
import com.businessreviewshub.data.requestDataDTO.BaseRequestDTO;
import com.businessreviewshub.data.requestDataDTO.EditProfileRequestDTO;
import com.businessreviewshub.data.requestDataDTO.SendSMSRequestDTO;
import com.businessreviewshub.data.responseDataDTO.UpdateUserResponseDTO;
import com.businessreviewshub.data.responseDataDTO.UserDTO;
import com.businessreviewshub.utils.DialogUtils;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.businessreviewshub.utils.UserAuth;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditProfileFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener {
    private EditText mUserFirstName, mUserPhoneNo, mUserPassword;
    private Button mUpdateSMS;
    private ImageView mUserPhoto;
    private int EDIT_PROFILE_MEDIA_PERMISSION_CODE = 39;
    private int EDIT_SELECT_IMAGE = 30;
    private String imgString;
    Bitmap bitmap = null;
    private boolean flag = true;
    // String imgString;


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
        mUserPhoto = (ImageView) view.findViewById(R.id.circleView);
        mUserFirstName.setText("" + mSessionManager.getEmployeeName());
        mUserPhoneNo.setText("" + mSessionManager.getEmployeePhone());
        mUserPassword.setText("" + mSessionManager.getEmployeePassword());
        progressDialog = DialogUtils.getProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mUpdateSMS.setOnClickListener(this);
        mUserPhoto.setOnClickListener(this);
        String userProfileImage = mSessionManager.getEmployeeProfileUrl();
        DownloadImage downloadImage = new DownloadImage();
        downloadImage.execute(userProfileImage);

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
                UserAuth userAuth = new UserAuth();
                userAuth.CleanAuthenticationInfo();
                Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
                startActivity(intent);

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
                    progressDialog.show();
                    callToWebService();
                }
                break;
            case R.id.circleView:
                callToRequestPermission();
                break;

        }
    }


    private void callToWebService() {
        String mUserName = mUserFirstName.getText().toString().trim();
        String mUserPhone = mUserPhoneNo.getText().toString().trim();
        String mUserPwd = mUserPassword.getText().toString().trim();
        if (flag == false) {
            bitmap = BitmapFactory.decodeFile(imgString);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
            byte[] byteArray = stream.toByteArray();
            imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }


        EditProfileRequestDTO editProfileRequestDTO = new EditProfileRequestDTO(mUserName, mUserPhone, mUserPwd, imgString);
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
        createAlertDialog(getResources().getString(R.string.str_prfile), getResources().getString(R.string.str_prfile_success));
        UpdateUserResponseDTO updateUserResponseDTO = UpdateUserResponseDTO.deserializeJson(data);
        String responseImgUrl = updateUserResponseDTO.getProfileImageUrl();
        String mUserName = mUserFirstName.getText().toString().trim();
        String mUserPhone = mUserPhoneNo.getText().toString().trim();
        String mUserPwd = mUserPassword.getText().toString().trim();
        UserDTO userDTO = new UserDTO();
        userDTO.setEmpCode(mSessionManager.getCompanyCode());
        userDTO.setEmpName(mUserName);
        userDTO.setPhoneNo(mUserPhone);
        userDTO.setEmpPwd(mUserPassword.getText().toString().trim());
        userDTO.setCompanyCode(mSessionManager.getEmployeeCode());
        userDTO.setCompanyName(mSessionManager.getEmployeeCompanyName());
        if (responseImgUrl.equals("false")) {
            userDTO.setProfileImage(mSessionManager.getEmployeeProfileUrl());
        } else {
            userDTO.setProfileImage(updateUserResponseDTO.getProfileImageUrl());
        }

        String test = mSessionManager.getEmployeeProfileUrl();
        userDTO.setCompanyLogo(mSessionManager.getEmployeeCompanyLogoUrl());
        UserAuth userAuth = new UserAuth();
        userAuth.saveAuthenticationInfo(userDTO, getContext().getApplicationContext());

    }

    private void callToRequestPermission() {
        requestPermissions(new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                EDIT_PROFILE_MEDIA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EDIT_PROFILE_MEDIA_PERMISSION_CODE && grantResults[1] == 0) {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, EDIT_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_SELECT_IMAGE && resultCode == Activity.RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgString = cursor.getString(columnIndex);
            cursor.close();
            mUserPhoto.setImageBitmap(BitmapFactory.decodeFile(imgString));
            mUserPhoto.requestFocus();
            flag = false;
        }

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];


            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;
                progressDialog.cancel();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                mUserPhoto.setImageBitmap(result);
                progressDialog.cancel();
            }
        }
    }

}
