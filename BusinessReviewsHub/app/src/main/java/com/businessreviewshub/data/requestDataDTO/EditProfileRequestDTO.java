package com.businessreviewshub.data.requestDataDTO;

/**
 * Created by shrinivas on 28-12-2016.
 */
public class EditProfileRequestDTO {
    private String name;
    private String phone;
    private String password;
    private String profileImage;

    public EditProfileRequestDTO(String name, String phone, String password, String profileImage) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
