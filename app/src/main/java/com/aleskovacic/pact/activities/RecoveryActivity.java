package com.aleskovacic.pact.activities;

/**
 * Created by Alex on 08.01.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aleskovacic.pact.R;

public class RecoveryActivity extends AppCompatActivity {

    private static final String TAG = "password_recovery_activity";

    Toolbar mToolbar;

    Fragment fragment;
    private AppBarLayout mAppBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.recovery);

        }


        if (savedInstanceState != null) {

            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment = new RecoveryFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        fragment.onActivityResult(requestCode, resultCode, data);
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