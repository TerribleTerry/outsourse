package com.aleskovacic.pact.activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.aleskovacic.pact.R;
import com.aleskovacic.pact.network.DataService;
import com.aleskovacic.pact.pojo.DataObject;
import com.aleskovacic.pact.pojo.DataResponse;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MapsMessageActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, GoogleMap.OnMyLocationChangeListener {

    private static final LatLng mMapStartLocation = new LatLng(50.448935, 30.523789);
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 101;
    private boolean isInfoWindowOpen;
    private boolean isSubscribedToMapLocationUpdates;
    private GoogleMap mMap;
    private ClusterManager<DataObject> mClusterManager;
    private Cluster<DataObject> clickedCluster;
    private DataObject clickedClusterItem;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFabMyLocation;
    private FloatingActionButton mFabMyLocationMessage;
    private DataResponse mTemp;
    private Snackbar mSnackbar;
    private static final String TAG = "MyApp";

    private DataService dataService;



    private Location myLocation;
    private SupportMapFragment mapFragment;


    private MessageResponse respbody;
    private MessageResult clickedMessage;
    private Get get;

    ArrayList<MessageResult> arrayMessageList;

    public String Messagetext = "";
    public String Personname = "";
    public String Messagetime = "";
    public String Messagelat = "";
    public String Messagelong = "";

    public  int arrn = 0;
    public double latitude;
    public double longtitude;
    private int mesnum;
    private int n;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    public LatLng messagelatlong;


    SharedPreferences sPref;

    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_STATE = "login_state";
    final String FB_LOGIN = "login_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        FacebookSdk.sdkInitialize(getApplicationContext());


        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mFabMyLocation = (FloatingActionButton) findViewById(R.id.btn_fab_my_location);
        mFabMyLocationMessage = (FloatingActionButton) findViewById(R.id.btn_fab_my_location_message);
        mFabMyLocationMessage.setOnClickListener(this);
        //tabLayout.setVisibility(View.GONE);


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

       if (ab != null) {
           ab.setDisplayHomeAsUpEnabled(true);
           final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
           upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
           getSupportActionBar().setHomeAsUpIndicator(upArrow);
           ab.setTitle("Let`s Chat?");

       }
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
         TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.GONE);


        //  TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
      //  tabLayout.addTab(tabLayout.newTab().setText("Дізнайся про що говорять поряд"));
      //  tabLayout.setTabMode(TabLayout.MODE_FIXED);
// название активити по центру
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // top padding for appBarLayout
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mAppBarLayout.setPadding(0, getResources().getDimensionPixelSize(resourceId), 0, 0);
            }
        }




        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                permissionExplanation();
            } else {
                requestLocationPermission();
            }
        } else {
            mapFragment.getMapAsync(this);
        }


        fetchMessages();

    }

    private void fetchMessages() {
        Get get = Get.retrofit.create(Get.class);
        Call<MessageResponse> call = get.getResult();
        call.enqueue(new Callback<MessageResponse>() {

            @Override
            public void onResponse(Response<MessageResponse> response, Retrofit retrofit) {
                ArrayList<MessageResult> arrayMessageList = response.body().getResult();
                for (int i = 0; i < arrayMessageList.size(); i++) {
                    arrn = i;
                    Messagetext = arrayMessageList.get(i).getMtext();
                    Personname = arrayMessageList.get(i).getLastname();
                    Messagetime = arrayMessageList.get(i).getMessagetime().toString();
                    Messagelat = arrayMessageList.get(i).getMessagelat();

                    Messagelong = arrayMessageList.get(i).getMessagelong();

                    latitude = Double.parseDouble(Messagelat);
                    longtitude = Double.parseDouble(Messagelong);
                    messagelatlong = new LatLng(latitude, longtitude);

                    addMessageMarker();

                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }




    private void permissionExplanation() {
        Snackbar.make(findViewById(R.id.coordinator_layout),
                R.string.snackbar_permission_location_explanation, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_permission_location_explanation_action,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestLocationPermission();
                            }
                        })
                .show();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this);
                    }
                } else {
                    permissionExplanation();
                }
            }
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            //  case R.id.signup:
            case android.R.id.home:
                onBackPressed();
                return true;
            // Intent loginintent = new Intent(this, signupactivity.class);
            //  startActivityForResult(loginintent, 1);
            case R.id.login:
                //startActivity(new Intent(this, FbloginActivity.class));
                //return true;
                sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
                Boolean loginstate = sPref.getBoolean(LOGIN_STATE, false);
                Boolean fbstate = sPref.getBoolean(FB_LOGIN, false);
                Log.e("Fb login", fbstate.toString() + loginstate.toString());
                if(loginstate) {
                    Intent logout = new Intent(this, LogoutActivity.class);
                    startActivityForResult(logout, 1);
                    return true;

                }else{
                    Intent login = new Intent(this, LogInActivity.class);
                    startActivityForResult(login, 1);
                    return true;
                }
                // case R.id.signin:
                //startActivity(new Intent(this, FbloginActivity.class));
                //return true;
                //    Intent signin = new Intent(this, signup.class);
                //   startActivityForResult(signin, 1);


            case R.id.addevent:
                Intent aboutus = new Intent(this, AboutUs.class);
                startActivityForResult(aboutus, 1);
                return true;

            case R.id.contactus:
                Intent connect = new Intent(this, ContactUs.class);
                startActivityForResult(connect, 1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Location camLoc = new Location("");
                camLoc.setLatitude(cameraPosition.target.latitude);
                camLoc.setLongitude(cameraPosition.target.longitude);
                if (myLocation != null && myLocation.distanceTo(camLoc) > 1) {
                    isSubscribedToMapLocationUpdates = false;

                } else {
                    isSubscribedToMapLocationUpdates = true;
                }
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!isInfoWindowOpen) {
                    ValueAnimator animation;
                    if (mAppBarLayout.getTranslationY() == 0.0) {
                        animation = ValueAnimator.ofFloat(0f, 1f);
                    } else {
                        animation = ValueAnimator.ofFloat(1f, 0f);
                    }

                    if (animation != null) {
                        animation.setDuration(200);
                        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mAppBarLayout.setTranslationY(-mAppBarLayout.getBottom() *
                                        (float) animation.getAnimatedValue());
                                mFabMyLocation.setAlpha((1 - (float) animation.getAnimatedValue()));
                                mFabMyLocationMessage.setAlpha((1 - (float) animation.getAnimatedValue()));
                            }
                        });
                        animation.start();
                    }
                }
                isInfoWindowOpen = false;
            }
        });






        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMapStartLocation, 10));
        mMap.setInfoWindowAdapter(new MapMessageAdapter(getLayoutInflater()));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){

            @Override
            public void onInfoWindowClick(Marker marker) {
                int pos = mHashMap.get(marker);
                Log.i("Position of arraylist", pos+"");

                Intent myintent = new Intent(getApplicationContext(), MessageChatActivity.class);

                myintent.putExtra("mesnumber", pos);
                startActivity(myintent);


            }
        });


    }

    @Override
    public void onMyLocationChange(Location location) {
        myLocation = location;
        if (!mFabMyLocation.isShown()) {
            mFabMyLocation.setVisibility(View.VISIBLE);
        } else if (isSubscribedToMapLocationUpdates) {
            updateMapLocation(location);
        }
    }

    private void updateMapLocation(Location location) {
        CameraUpdate cm = CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(),
                myLocation.getLongitude()), 12);
        mMap.animateCamera(cm);
       // drawable.setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.MULTIPLY);
    }
    private void addMessageMarker() {
        mesnum = arrn;
        // n = mesnum;
        Log.i(TAG, "индекс из n" + mesnum);

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(messagelatlong)
                .title(Messagetext)
                .snippet(Personname)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_18dp))


        );


        mHashMap.put(marker, mesnum);
        Log.i(TAG, "индекс маркера" + mesnum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fab_my_location:
                if(myLocation != null){
                    Log.e("my location= ", myLocation.toString());
                    updateMapLocation(myLocation);
                    break;
                }else{
                    Snackbar snackbar = Snackbar
                            .make(mCoordinatorLayout, "Please turn on gps", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    break;

                }
            // case R.id.btn_fab_my_location_message:
            //                   mMap.addMarker(new MarkerOptions()
            //         .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
            //       .title("Unbelivable!"));
            case R.id.btn_fab_my_location_message:

                sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
                Boolean loginstate = sPref.getBoolean(LOGIN_STATE, false);
                Profile profile = Profile.getCurrentProfile().getCurrentProfile();

                if(loginstate || profile != null) {
                    Intent logout = new Intent(this, GeoMessageActivity.class);
                    startActivityForResult(logout, 1);
                    break;


                }else{
                    Intent login = new Intent(this, LogInActivity.class);
                    startActivityForResult(login, 1);
                    break;





                }

                //case R.id.login:
                // Intent loginintent = new Intent(this, FbloginActivity.class);
                // startActivityForResult(loginintent, 1);
        }


    }





}


