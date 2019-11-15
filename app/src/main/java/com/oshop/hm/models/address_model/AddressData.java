package com.oshop.hm.models.address_model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddressData {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("data")
    @Expose
    private List<AddressDetails> data = new ArrayList<AddressDetails>();
    @SerializedName("address_id")
    @Expose
    private String address_id;
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
    public List<AddressDetails> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<AddressDetails> data) {
        this.data = data;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getAddressId() {
        return address_id;
    }

    /**
     *
     * @param address_id
     *     The message
     */
    public void setAddressId(String address_id) {
        this.message = address_id;
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
