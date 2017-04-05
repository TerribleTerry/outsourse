package com.aleskovacic.pact.network;

/**
 * Created by Alex on 17.03.2017.
 */

import com.aleskovacic.pact.pojo.UserPosts;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

public interface GetUserPosts {

    @GET("getuserposts.php")
    Call<UserPosts> getPosts(@Query("userfbidoremail") String userfbidoremail);


    //  @GET("getuserbyfb.php")
    //  Call<Profile> getUsersbyfb(@Query("fb_id") String fb_id);

    Retrofit retrofituserposts = new Retrofit.Builder()
            .baseUrl("http://nearme.s-host.net/RegisterFB/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
