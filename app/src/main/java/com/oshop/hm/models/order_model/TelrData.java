package com.oshop.hm.models.order_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by samsung on 12/09/18.
 */

public class TelrData {

    @SerializedName("order_url")
    @Expose
    private String order_url;

    @SerializedName("order_ref")
    @Expose
    private String order_ref;

    public String getOrder_url() {
        return order_url;
    }

    public void setOrder_url(String order_url) {
        this.order_url = order_url;
    }

    public String getOrder_ref() {
        return order_ref;
    }

    public void setOrder_ref(String order_ref) {
        this.order_ref = order_ref;
    }
}
