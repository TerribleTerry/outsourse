package com.aleskovacic.pact.activities;


import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.aleskovacic.pact.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class
GeoMessageActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    EditText message;
    SharedPreferences sPref;

    private static final String MESSAGE_URL = "http://nearme.s-host.net/RegisterFB/postmessages.php";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public Double currentLatitude;
    public Double currentLongitude;


    public static final String KEY_MTEXT = "mtext";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_MESSAGETIME = "messagetime";
    public static final String KEY_MESSAGELAT = "messagelat";
    public static final String KEY_MESSAGELONG = "messagelong";
    public static final String KEY_USERSURNAME = "usersurname";


    public static final String KEY_FBID = "fb_id";

public static final String KEY_EMAIL = "email";

    public  static String lastname = "lastname";
    public static String fb_id = "";
    public static String email = "";
    public static String usersurname = "";

    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_STATE = "login_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geomessage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        Log.e("null error ", "messagelat");

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            ab.setTitle("Your message");

        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        message = (EditText) findViewById(R.id.message);


        FacebookSdk.sdkInitialize(getApplicationContext());

        //boolean loggedIn = AccessToken.getCurrentAccessToken() != null;




        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);

        String savedemail = sPref.getString(SAVED_EMAIL, "");






        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .addApi(com.google.android.gms.appindexing.AppIndex.API).build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_support, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_send: {


                if (message.length() > 0){

                    sendMessage();

                } else {

                    Toast.makeText(getApplicationContext(), getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            default: {

                break;
            }
        }


        return false;
    }



    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }


    private void sendMessage() {
        Profile profile = Profile.getCurrentProfile();
       SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        long unixTime = System.currentTimeMillis() / 1000L;
        String time = String.valueOf(unixTime);
        Log.e("messagelat", "time " + time);

        Log.e("Message time in unix", time);


        final String mtext = message.getText().toString().trim();
        Log.e("messagelat", "mtext " + mtext);

        final String messagetime = sdfDate.format(now);
        Log.e("messagelat", "messagetime " + messagetime);

        //  final String messagetime = time;
        Log.e("messagelat", "beforecurrentLatitude ");
        final String messagelat = currentLatitude.toString();
        Log.e("messagelat", "currentLatitude " + messagelat);
        final String messagelong = currentLongitude.toString();
        Log.e("messagelat", "currentLongitude " + messagelong);
        if(profile != null) {
           lastname = profile.getName().trim();

            fb_id = Profile.getCurrentProfile().getId();
            usersurname = fb_id;
        }else{
            sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
            String savedemail = sPref.getString(SAVED_EMAIL, "");
            email = savedemail;
            usersurname = email;
        }

        //info.getText().toString().trim().toLowerCase();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, MESSAGE_URL,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(GeoMessageActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GeoMessageActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){


            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_MTEXT, mtext);
                params.put(KEY_LASTNAME, lastname);
                params.put(KEY_MESSAGETIME, messagetime);
                params.put(KEY_MESSAGELAT, messagelat);
                params.put(KEY_MESSAGELONG, messagelong);
                params.put(KEY_USERSURNAME, usersurname);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }





    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    @Override
    public void onClick(View v) {

        sendMessage();
        Intent intent = new Intent();
        intent.putExtra("messagetext", message.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
}


