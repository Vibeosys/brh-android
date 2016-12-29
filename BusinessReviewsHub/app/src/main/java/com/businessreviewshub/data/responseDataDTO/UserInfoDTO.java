package com.businessreviewshub.data.responseDataDTO;

import java.io.Serializable;

/**
 * Created by shrinivas on 29-12-2016.
 */
public class UserInfoDTO implements Serializable {
    private String companyLogo;
    private String companyName;
    private String profileImage;

    public UserInfoDTO(String companyLogo, String companyName, String profileImage) {
        this.companyLogo = companyLogo;
        this.companyName = companyName;
        this.profileImage = profileImage;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
