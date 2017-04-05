package com.aleskovacic.pact.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aleskovacic.pact.R;
import com.aleskovacic.pact.network.GetUserPosts;
import com.aleskovacic.pact.network.Shaw;
import com.aleskovacic.pact.pojo.Post;
import com.aleskovacic.pact.pojo.Profile;
import com.aleskovacic.pact.pojo.User;
import com.aleskovacic.pact.pojo.UserPosts;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Myprofile extends AppCompatActivity implements View.OnClickListener{
    TextView MyName;
    TextView editPhoto;

    public String posttext = "";
    public String posttime = "";

    public String username = "";
    public String myid = "";
    public String PhotoURL = "";
    private static final String TAG = "Myprofile";

    public static final String KEY_FBID = "fb_id";


    SharedPreferences sPref;

    final String SAVED_EMAIL = "saved_email";

public String userfbidoremail = "";
public String useridentifier = "";
public String email = "";
public String fb_id = "";
    ImageView myPhoto;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        MyName = (TextView) findViewById(R.id.MyName);
        editPhoto = (TextView) findViewById(R.id.editPhoto);
    editPhoto.setOnClickListener((View.OnClickListener) this);

     myPhoto = (ImageView) findViewById(R.id.myPhoto);
        //если пользователь залогинен обычным способом, то берем его мыло из префов
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

     email = sPref.getString(SAVED_EMAIL, "");
        Log.i(TAG, "мой юзернейм из префов" + email);



    if(email.equals("")){
        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile().getCurrentProfile();
        fb_id = profile.getId();
        Log.i(TAG, "идентификация через фб: " + fb_id);
useridentifier = fb_id;
    }else{
        useridentifier = email;
        Log.i(TAG, "идентификация через почту: " + email);

    }
        //String email = "a.o.rynda@gmail.com";


// Берем данные пользователя
        Shaw shaw = Shaw.retrofituser.create(Shaw.class);
        Call<Profile> calluser = shaw.getUsers(useridentifier);
        calluser.enqueue(new Callback<Profile>() {


            @Override
            public void onResponse(Response<Profile> response, Retrofit retrofit) {
                List<User> arrayusers = response.body().getUsers();
                username = arrayusers.get(0).getUsername();
                myid = arrayusers.get(0).getId();
                    PhotoURL = arrayusers.get(0).getPhoto(); //здесь из массива берется url фотки
                Log.i(TAG, "мой юзекнейм" + "айди: " + myid + " " + "юзернейм " + " " + PhotoURL);
                MyName.setText(username);
                if(arrayusers.get(0).getPhoto() != "") {
                    Log.e("Photourl","Photourl" + PhotoURL);

                    Picasso.with(Myprofile.this).load(PhotoURL).into(myPhoto); //присваивается url-фотки изображению
                }else{
                    myPhoto.setImageResource(R.drawable.photo_circle);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });



        // Берем посты пользователя

 userfbidoremail = useridentifier;
    Log.i(TAG, "идентификация постов через: " + userfbidoremail);


    final LinearLayout linLayoutPosts = (LinearLayout) findViewById(R.id.linLayoutposts);
    registerForContextMenu(linLayoutPosts);

    final LayoutInflater ltInflater = getLayoutInflater();

    GetUserPosts posts = GetUserPosts.retrofituserposts.create(GetUserPosts.class);
        Call<UserPosts> getposts = posts.getPosts(userfbidoremail);
        getposts.enqueue(new Callback<UserPosts>() {


            @Override
            public void onResponse(Response<UserPosts> response, Retrofit retrofit) {
                List<Post> arrayposts = response.body().getPosts();

                for(int i = 0; i < arrayposts.size(); i++) {
                    View postitem = ltInflater.inflate(R.layout.post_item, linLayoutPosts, false);

                    TextView postText = (TextView) postitem.findViewById(R.id.posttext);

                    TextView  postTime = (TextView) postitem.findViewById(R.id.posttime);

                    posttext = arrayposts.get(i).getMtext();
                    posttime = arrayposts.get(i).getMessagetime();
                    postText.setText(posttext);
                    postTime.setText(posttime);
                    Log.i(TAG, "посты юзера" + posttext + " " + posttime);
                    linLayoutPosts.addView(postitem);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });




    }


    @Override
    public void onClick(View v) {
        if(v == editPhoto){
            String userid = myid;
            Intent editphoto = new Intent(this, Phototest.class);
            editphoto.putExtra("userid", userid);

            startActivityForResult(editphoto, 1);

               }

    }
}
