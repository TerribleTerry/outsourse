package com.aleskovacic.pact.activities;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.aleskovacic.pact.R;
import com.facebook.FacebookSdk;
import com.facebook.Profile;


public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sPref;

    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_STATE = "login_state";
    final String FB_LOGIN = "login_state";


    Fragment fragmentlogout;
    Fragment fragmentsignin;
    Fragment Fblogingragment;

    // Button signupbtn1;

    Boolean fbstate;

    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        FacebookSdk.sdkInitialize(getApplicationContext());



        //  signupbtn1 = (Button) findViewById(R.id.signupbtn1);
        //   signupbtn1.setOnClickListener(this);


        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

        Boolean loginstate = sPref.getBoolean(LOGIN_STATE, false);
         fbstate = sPref.getBoolean(FB_LOGIN, false);

        Log.e("fbloginaccestocken", fbstate.toString());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            ab.setTitle("Login");

        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        Fblogingragment = new FbloginFragment();


        fragmentsignin = new LoginFragment();

        Profile profile = Profile.getCurrentProfile().getCurrentProfile();
        if(profile != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body2, Fblogingragment).commit();
            //  signupbtn1.setVisibility(View.GONE);


        } else{


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container_body1, fragmentsignin).commit();

            fragmentManager.beginTransaction().replace(R.id.container_body2, Fblogingragment).commit();

    }

}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

    }



}





