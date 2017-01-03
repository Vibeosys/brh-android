package com.businessreviewshub.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import com.businessreviewshub.utils.NetworkUtils;
import com.businessreviewshub.utils.ServerRequestConstants;
import com.businessreviewshub.utils.ServerSyncManager;
import com.businessreviewshub.utils.UserAuth;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

public class EditProfileFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, View.OnClickListener, MainActivity.DataReceived {
    private EditText mUserFirstName, mUserPhoneNo, mUserPassword;
    private Button mUpdateSMS;
    private ImageView mUserPhoto;
    private int EDIT_PROFILE_MEDIA_PERMISSION_CODE = 39;
    private int EDIT_SELECT_IMAGE = 30;
    private String imgString;
    Bitmap bitmap = null;
    private boolean flag = true;
    // String imgString;
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

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
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDataReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mUpdateSMS.setOnClickListener(this);
        mUserPhoto.setOnClickListener(this);
        String userProfileImage = mSessionManager.getEmployeeProfileUrl();
        if (NetworkUtils.isActiveNetworkAvailable(getContext())) {
            DownloadImage downloadImage = new DownloadImage();
            downloadImage.execute(userProfileImage);
        } else if (!NetworkUtils.isActiveNetworkAvailable(getContext())) {
            createAlertDialog(getResources().getString(R.string.app_name), getResources().getString(R.string.str_no_internet));
        }


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
                    if (NetworkUtils.isActiveNetworkAvailable(getContext())) {
                        progressDialog.show();
                        callToWebService();
                    } else if (!NetworkUtils.isActiveNetworkAvailable(getContext())) {
                        createAlertDialog(getResources().getString(R.string.app_name), getResources().getString(R.string.str_no_internet));
                    }

                }
                break;
            case R.id.circleView:
                //callToRequestPermission();
                openGallery1();
                break;

        }
    }

    public void openGallery1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.str_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    private void callToWebService() {
        String mUserName = mUserFirstName.getText().toString().trim();
        String mUserPhone = mUserPhoneNo.getText().toString().trim();
        String mUserPwd = mUserPassword.getText().toString().trim();
        if (flag == false) {
            try {
                bitmap = BitmapFactory.decodeFile(imgString);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
                byte[] byteArray = stream.toByteArray();
                imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (Exception e) {
                Log.e("profile", "" + e.toString());
            }
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
            mUserFirstName.setError(getResources().getString(R.string.str_pro_user));
            return false;
        }
        if (TextUtils.isEmpty(mUserPhone)) {
            mUserPhoneNo.setError(getResources().getString(R.string.str_pro_ph));
            return false;
        }
        if (TextUtils.isEmpty(mUserPwd)) {
            mUserPassword.setError(getResources().getString(R.string.str_pro_pwd));
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
        if (flag == false) {
            userDTO.setProfileImage(updateUserResponseDTO.getProfileImageUrl());
        } else if (flag == true) {
            userDTO.setProfileImage(mSessionManager.getEmployeeProfileUrl());
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
        /*if (requestCode == EDIT_SELECT_IMAGE && resultCode == Activity.RESULT_OK
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
        }*/

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(getContext(), R.string.str_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }

    }

    @Override
    public void onDataReceived(Intent data) {
        handleCropResult(data);
    }

    @Override
    public void onErrorReceived(Intent data) {
        handleCropError(data);
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


    public void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".png";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), destinationFileName)));

       /* uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);*/

        uCrop.start(getActivity());
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("Test", "handleCropError: ", cropError);
            Toast.makeText(getContext(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), R.string.str_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // ResultActivity.startWithUri(SampleActivity.this, resultUri);
            saveCropImage(resultUri);
        } else {
            Toast.makeText(getContext(), R.string.str_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCropImage(Uri resultUri) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(resultUri.getPath()).getAbsolutePath(), options);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Uri imageUri = resultUri;
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    copyFileToDownloads(resultUri);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Test", imageUri.toString(), e);
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.str_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());

        File saveFile = new File(downloadsDirectoryPath, filename);

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
        imgString = saveFile.getAbsolutePath();
        mUserPhoto.setImageURI(Uri.fromFile(saveFile));
        flag = false;
        // showNotification(saveFile);
    }
}
