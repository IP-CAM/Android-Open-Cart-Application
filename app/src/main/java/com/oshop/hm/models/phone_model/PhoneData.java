package com.oshop.hm.models.phone_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oshop.hm.models.banner_model.BannerDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 04/08/18.
 */

public class PhoneData {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private String data ;
    @SerializedName("message")
    @Expose
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
