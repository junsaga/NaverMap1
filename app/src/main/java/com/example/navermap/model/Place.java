package com.example.navermap.model;

import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("title")
    private String title;

    @SerializedName("category")
    private String category;

    @SerializedName("address")
    private String address;

    @SerializedName("mapx")
    private String mapX;

    @SerializedName("mapy")
    private String mapY;

    // Getter 메서드 등 필요한 코드 작성

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getMapX() {
        return mapX;
    }

    public String getMapY() {
        return mapY;
    }
}
