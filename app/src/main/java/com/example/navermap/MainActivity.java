package com.example.navermap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.navermap.api.AddressApi;
import com.example.navermap.api.NetworkClient;
import com.example.navermap.config.Config;
import com.example.navermap.model.PlaceResponse;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.block.googleplace.adapter.PlaceAdapter;
import com.block.googleplace.api.NetworkClient;
import com.block.googleplace.api.PlaceApi;
import com.block.googleplace.config.Config;
import com.block.googleplace.model.Place;
import com.block.googleplace.model.PlaceList;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editKeyword;
    ImageView btnSearch;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    PlaceAdapter adapter;
    ArrayList<Place> placeArrayList = new ArrayList<>();

    // 내 위치 가져오기 위한 멤버변수
    LocationManager locationManager;
    LocationListener locationListener;

    double lat;
    double lng;

    int radius = 2000;  // 미터 단위
    String keyword;

    boolean isLocationReady;

    String pagetoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editKeyword = findViewById(R.id.editKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCont = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCont){

                    if(pagetoken != null){
                        addNetworkData();
                    }

                }

            }
        });

        // 폰의 위치를 가져오기 위해서는, 시스템서비스로부터 로케이션 매니져를
        // 받아온다.
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 로케이션 리스터를 만든다.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 여러분들의 로직 작성.

                // 위도 가져오는 코드.
                // location.getLatitude();
                // 경도 가져오는 코드.
                // location.getLongitude();

                lat = location.getLatitude();
                lng = location.getLongitude();

                isLocationReady = true;
            }
        };

        if( ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    100);
            return;
        }

        // 위치기반 허용하였으므로,
        // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000,
                -1,
                locationListener);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isLocationReady == false){
                    Snackbar.make(btnSearch,
                            "아직 위치를 잡지 못했습니다. 잠시후 다시 검색하세요.",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }

                keyword = editKeyword.getText().toString().trim();

                Log.i("AAA", keyword);

                if(keyword.isEmpty()){
                    Log.i("AAA", "isEmpty");
                    return;
                }

                getNetworkData();

            }
        });

    }

    private void addNetworkData() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        AddressApi api = retrofit.create(AddressApi.class);

        Call<PlaceResponse> call = api.getNearbyPlaces(
                "카페", // 검색어 (원하는 키워드)
                "126.9783882,37.5666103", // 중심 좌표 (경도,위도)
                1000, // 검색 반경 (1000m)
                "random", // 랜덤 정렬
                Config.Client_ID // 네이버 지도 API 클라이언트 ID
        );
        call.enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    PlaceResponse placeResponse = response.body();

                    pagetoken = placeResponse.next_page_token;

                    placeArrayList.addAll( placeList.results );

                    adapter.notifyDataSetChanged();

                }else{

                }

            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void getNetworkData() {

        Log.i("AAA", "getNetworkData");

        progressBar.setVisibility(View.VISIBLE);

        placeArrayList.clear();

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlaceList("ko",
                lat+","+lng,
                radius,
                Config.GOOGLE_API_KEY,
                keyword);

        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {

                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    PlaceList placeList = response.body();

                    pagetoken = placeList.next_page_token;

                    placeArrayList.addAll(placeList.results);

                    adapter = new PlaceAdapter(MainActivity.this, placeArrayList);
                    recyclerView.setAdapter(adapter);

                }{

                }
            }

            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){

            if( ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION} ,
                        100);
                return;
            }

            // 위치기반 허용하였으므로,
            // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    -1,
                    locationListener);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.menuMap){

            Intent intent = new Intent(MainActivity.this, PlaceActivity.class);
            intent.putExtra("placeArrayList", placeArrayList);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
}