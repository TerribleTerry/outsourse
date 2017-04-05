package com.aleskovacic.pact.network;

import com.aleskovacic.pact.pojo.Profile;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Alex on 03.02.2017.
 */



public interface Shaw {
    @GET("getsignedupuser.php")
    Call<Profile> getUsers(@Query("useridentifier") String useridentifier);


  //  @GET("getuserbyfb.php")
  //  Call<Profile> getUsersbyfb(@Query("fb_id") String fb_id);

    Retrofit retrofituser = new Retrofit.Builder()
            .baseUrl("http://nearme.s-host.net/RegisterFB/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}