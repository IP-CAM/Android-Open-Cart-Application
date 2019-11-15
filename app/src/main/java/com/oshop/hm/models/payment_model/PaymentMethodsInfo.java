
package com.oshop.hm.models.payment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PaymentMethodsInfo {

    @SerializedName("key")
    @Expose
    private String  key;
    @SerializedName("code")
    @Expose
    private String  code; // method
    @SerializedName("title")
    @Expose
    private String title; // name
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("sort_order")
    @Expose
    private String sort_order; // name

    // SETTER & GETTER


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }


    /*

    @SerializedName("public_key")
    @Expose
    private String publicKey;

    @SerializedName("payment_currency")
    @Expose
    private String paymentCurrency;

    @SerializedName("active")
    @Expose
    private String active;

    @SerializedName("environment")
    @Expose
    private String environment;

    */



}

