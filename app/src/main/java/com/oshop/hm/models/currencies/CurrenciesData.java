package com.oshop.hm.models.currencies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oshop.hm.models.coupons_model.CouponsInfo;

import java.util.List;

/**
 * Created by samsung on 01/08/18.
 */

public class CurrenciesData {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<CurrenciesInfo> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CurrenciesInfo> getData() {
        return data;
    }

    public void setData(List<CurrenciesInfo> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
