package com.asolomkin.loftmoney;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status") String status;
    @SerializedName("id") int userId;
    @SerializedName("auth_token") String accessToken;

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
