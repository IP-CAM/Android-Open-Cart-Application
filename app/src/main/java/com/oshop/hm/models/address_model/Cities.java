package com.oshop.hm.models.address_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samsung on 02/08/18.
 */

public class Cities {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<CityDetails> data = new ArrayList<CityDetails>();
    @SerializedName("message")
    @Expose
    private String message;

    /**
     *
     * @return
     *     The success
     */
    public String getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     *     The success
     */
    public void setSuccess(String success) {
        this.success = success;
    }

    /**
     *
     * @return
     *     The data
     */
    public List<CityDetails> getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(List<CityDetails> data) {
        this.data = data;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }


}
