package com.aleskovacic.pact.activities;

/**
 * Created by Alex on 29.11.2016.
 */

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
import android.view.MenuItem;

import com.aleskovacic.pact.R;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

public class LogoutActivity extends AppCompatActivity {

    SharedPreferences sPref;

    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_STATE = "login_state";
    final String FB_LOGIN = "login_state";

    Fragment fragmentlogout;
    Fragment fragmentsignin;
    Fragment Fblogingragment;
    private AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        FacebookSdk.sdkInitialize(getApplicationContext());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);


        }

        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

      // Boolean loginstate = sPref.getBoolean(LOGIN_STATE, false);
      //  Boolean fbstate = sPref.getBoolean(FB_LOGIN, false);

        fragmentlogout = new LogoutFragment();
        Fblogingragment = new FbloginFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (accessToken != null) {
            fragmentManager.beginTransaction().replace(R.id.container_body2, Fblogingragment).commit();

        }else if(accessToken == null) {
            fragmentManager.beginTransaction().replace(R.id.container_body1, fragmentlogout).commit();
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




}
