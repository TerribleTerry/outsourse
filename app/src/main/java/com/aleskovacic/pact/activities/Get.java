package com.aleskovacic.pact.activities;

import com.aleskovacic.pact.pojo.CommentsResponse;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Alex on 03.12.2016.
 */

public interface Get {
    @GET("getposts.php")
    Call<MessageResponse> getResult();



    @GET("getcomments.php")
    Call<CommentsResponse> getComments(@Query("replyonpostId") String replyonpostId);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://nearme.s-host.net/RegisterFB/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    // Gson gson = new GsonBuilder()
    //     .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    //    .create();


}