package com.aleskovacic.pact.network;

import com.aleskovacic.pact.pojo.DataResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface DataService {
    @GET("index.php")
    Call<DataResponse> getEvents(@Query("time") int time);

    @GET("search.php")
    Call<DataResponse> search(@Query("query") String query);
}