package com.example.navermap.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address {
    @SerializedName("roadAddress")
    private String roadAddress;
    @SerializedName("jibunAddress")
    private String jibunAddress;
    @SerializedName("englishAddress")
    private String englishAddress;
    @SerializedName("x")
    private String x;
    @SerializedName("y")
    private String y;
    @SerializedName("distance")
    private double distance;
    private List<AddressElement> addressElements;

}
