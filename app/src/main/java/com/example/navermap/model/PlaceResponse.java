package com.example.navermap.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceResponse {
    @SerializedName("items")
    private List<Place> places;

    // Getter 메서드 등 필요한 코드 작성

    public List<Place> getPlaces() {
        return places;
    }
}
