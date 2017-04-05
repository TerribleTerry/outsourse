package com.aleskovacic.pact.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aleskovacic.pact.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alex on 16.11.2016.
 */

public class
FbloginFragment  extends Fragment {

    SharedPreferences sPref;



    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERFULLNAME = "usersurname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FBID = "fb_id";

    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_USERNAME = "login_state";
    final String FB_LOGIN = "fb_login_state";
    final String USUAL_LOGIN = "usual_login_state";
    final String LOGIN_STATE = "login_state";

    private static final String SIGNUPFBUSER_URL = "http://nearme.s-host.net/RegisterFB/fbsignup.php";
    public static final String KEY_PASSWORD = "password";


    private CallbackManager callbackManager;
    public boolean wheaterlogged;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

String username, usersurname, fb_id, email, password;


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Profile.getCurrentProfile().getFirstName();
            displayMessage(profile);

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.v("LoginActivity Response ", response.toString());

                            try {
                                username = object.getString("first_name");

                                email = object.getString("email");

                                fb_id = object.getString("id");
                                usersurname = object.getString("first_name");

                                wheaterlogged = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
           parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
            registerfbuser();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };




    private void saveuserstate() {

        Profile profile = Profile.getCurrentProfile().getCurrentProfile();

        fb_id = profile.getId();
        if(profile != null) {
            sPref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_EMAIL, email);
            ed.putString(KEY_FBID, fb_id);
            ed.putBoolean(LOGIN_STATE, true);
            ed.putBoolean(FB_LOGIN, true);
            ed.putBoolean(USUAL_LOGIN, false);
            ed.putString(LOGIN_USERNAME, username);
            ed.commit();
            Toast.makeText(getContext(), "You are logged with fb", Toast.LENGTH_SHORT).show();
        }else{
            sPref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putBoolean(LOGIN_STATE, false);
            ed.putBoolean(FB_LOGIN, false);
            ed.commit();
            Toast.makeText(getContext(), "Not logged with fb any more", Toast.LENGTH_SHORT).show();

        }
    }


    public FbloginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        AppEventsLogger.activateApp(getContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {

            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentfblogin, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);}
    private void registerfbuser() {


        Profile profile = Profile.getCurrentProfile().getCurrentProfile();
username = profile.getName();
        fb_id = profile.getId();


        Log.e("Insert data", fb_id + username);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUPFBUSER_URL,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {

                        Log.e("DB response: ", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("DB error: ", error.toString());

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put(KEY_FBID, fb_id);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void displayMessage(Profile profile){
        if(profile != null){
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
      //  saveuserstate();
    }

}
