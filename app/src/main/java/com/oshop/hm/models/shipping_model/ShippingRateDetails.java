
package com.oshop.hm.models.shipping_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShippingRateDetails {

    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("shippingMethods")
    @Expose
    private List<ShippingService> shippingMethods;
    // Old : private ShippingMethods shippingMethods;


    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public List<ShippingService> getShippingMethods() {
        return shippingMethods;
    }

    public void setShippingMethods(List<ShippingService> shippingMethods) {
        this.shippingMethods = shippingMethods;
    }

}
