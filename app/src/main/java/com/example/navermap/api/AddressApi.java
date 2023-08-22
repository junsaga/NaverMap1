package com.example.navermap.api;

import com.example.navermap.model.AddressResponse;
import com.example.navermap.model.PlaceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface  AddressApi{
    @GET("/v1/search/place.json")
    Call<PlaceResponse> getNearbyPlaces(
            @Query("query") String query, // 검색어
            @Query("coords") String coords, // 중심 좌표 (경도,위도)
            @Query("radius") int radius, // 검색 반경 (미터 단위)
            @Query("sort") String sort, // 정렬 방식 (distance or random)
            @Query("key") String key // 네이버 지도 API 키
    );
}