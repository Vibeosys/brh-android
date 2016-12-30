package com.businessreviewshub.data.responseDataDTO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by shrinivas on 30-12-2016.
 */
public class UpdateUserResponseDTO {
    private static final String TAG = UpdateUserResponseDTO.class.getSimpleName();
    private String profileImageUrl;

    public UpdateUserResponseDTO(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static UpdateUserResponseDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        UpdateUserResponseDTO updateUserResponseDTO = null;
        try {
            updateUserResponseDTO = gson.fromJson(serializedString, UpdateUserResponseDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization RegisterResponseData" + e.toString());
        }
        return updateUserResponseDTO;
    }
}
