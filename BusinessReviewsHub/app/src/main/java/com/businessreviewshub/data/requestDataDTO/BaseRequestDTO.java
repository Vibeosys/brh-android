package com.businessreviewshub.data.requestDataDTO;

import com.businessreviewshub.data.BaseDTO;

/**
 * Created by shrinivas on 27-12-2016.
 */
public class BaseRequestDTO extends BaseDTO {
    private UserRequestDTO user;
    private String data;

    public BaseRequestDTO() {
    }

    public BaseRequestDTO(UserRequestDTO user, String data) {
        this.user = user;
        this.data = data;
    }

    public UserRequestDTO getUser() {
        return user;
    }

    public void setUser(UserRequestDTO user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
