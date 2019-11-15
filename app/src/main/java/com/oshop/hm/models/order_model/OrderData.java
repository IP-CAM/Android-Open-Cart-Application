package com.oshop.hm.models.order_model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderData {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<OrderDetails> data = new ArrayList<OrderDetails>();
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("telr_data")
    @Expose
    private TelrData telr_data;

    public TelrData getTelr_data() {
        return telr_data;
    }

    public void setTelr_data(TelrData telr_data) {
        this.telr_data = telr_data;
    }

    /**
     *
     * @return
     * The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The data
     */
    public List<OrderDetails> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<OrderDetails> data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }


}
