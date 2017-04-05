package com.aleskovacic.pact.activities;

import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.aleskovacic.pact.R;

public class AboutUs extends AppCompatActivity {

    TextView textaboutus;
    private AppBarLayout mAppBarLayout;

    SharedPreferences sPref;

    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_STATE = "login_state";
    final String FB_LOGIN = "login_state";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView textaboutus = (TextView) findViewById(R.id.textaboutus);

        textaboutus.setText(R.string.aboutustext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("About Us");

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
