package com.example.navermap.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressElement {
    private List<String> types;
    @SerializedName("longName")
    private String longName;
    @SerializedName("shortName")
    private String shortName;
    @SerializedName("code")
    private String code;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
