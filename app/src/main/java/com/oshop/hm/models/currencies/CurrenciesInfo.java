package com.oshop.hm.models.currencies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by samsung on 01/08/18.
 */

public class CurrenciesInfo {


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("symbol_left")
    @Expose
    private String symbol_left;

    @SerializedName("symbol_right")
    @Expose
    private String symbol_right;

    @SerializedName("decimal_point")
    @Expose
    private char decimal_point;

    @SerializedName("thousands_point")
    @Expose
    private char thousands_point;

    @SerializedName("decimal_places")
    @Expose
    private char decimal_places;

    @SerializedName("value")
    @Expose
    private Double value;

    @SerializedName("last_updated")
    @Expose
    private String last_updated;

    @SerializedName("language_id")
    @Expose
    private int language_id;

    @SerializedName("currency_rate")
    @Expose
    private float currency_rate;


    // ###########################################################################


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol_left() {
        return symbol_left;
    }

    public void setSymbol_left(String symbol_left) {
        this.symbol_left = symbol_left;
    }

    public String getSymbol_right() {
        return symbol_right;
    }

    public void setSymbol_right(String symbol_right) {
        this.symbol_right = symbol_right;
    }

    public char getDecimal_point() {
        return decimal_point;
    }

    public void setDecimal_point(char decimal_point) {
        this.decimal_point = decimal_point;
    }

    public char getThousands_point() {
        return thousands_point;
    }

    public void setThousands_point(char thousands_point) {
        this.thousands_point = thousands_point;
    }

    public char getDecimal_places() {
        return decimal_places;
    }

    public void setDecimal_places(char decimal_places) {
        this.decimal_places = decimal_places;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public float getCurrency_rate() {
        return currency_rate;
    }

    public void setCurrency_rate(float currency_rate) {
        this.currency_rate = currency_rate;
    }
}
