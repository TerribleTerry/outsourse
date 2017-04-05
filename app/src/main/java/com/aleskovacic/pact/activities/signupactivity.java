package com.aleskovacic.pact.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aleskovacic.pact.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class signupactivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REGISTERUSER_URL = "http://nearme.s-host.net/RegisterFB/signup.php";

    SharedPreferences sPref;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    EditText signupUsername, signupPassword, signupEmail;
    String username, password, email;

    //Button signupBtn;

    TextView mLabelTerms;
TextView login;
    AppBarLayout.LayoutParams layoutparams;
    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_USERNAME = "login_state";
    final String LOGIN_STATE = "login_state";
    final String FB_LOGIN = "fb_login_state";
    final String USUAL_LOGIN = "usual_login_state";
    private AppBarLayout mAppBarLayout;
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsignup);


        signupUsername = (EditText) findViewById(R.id.input_name);
        signupPassword = (EditText) findViewById(R.id.input_password);
        signupEmail = (EditText) findViewById(R.id.input_email);

        login = (TextView) findViewById(R.id.link_login) ;
        login.setOnClickListener(this);


        //signupBtn = (Button) findViewById(R.id.btn_signup);
        //signupBtn.setOnClickListener(this);






        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();



        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_send: {
                signupuser();

            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
       // if (v == signupBtn){
       //     signupuser();
       // }
        if(v == login){
            finish();
        }
    }

    private void signupuser() {


        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(signupactivity.this,
                R.style.progress_bar_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        username =signupUsername.getText().toString().trim();
        email =signupEmail.getText().toString().trim();
        password =signupPassword.getText().toString().trim();

Log.e("userprofile: ", username + email + password);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTERUSER_URL,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        String answer = response.toString();
                        if (answer.equals("email already exist")) {
                            Toast.makeText(signupactivity.this, "Користувач з такою поштою вже існує", Toast.LENGTH_LONG).show();

                        } else if(answer.equals("registered")){
                            Toast.makeText(signupactivity.this, "Привіт, " + username + "!", Toast.LENGTH_LONG).show();
                            saveuserdata();
                        }else{
                            Toast.makeText(signupactivity.this, response, Toast.LENGTH_LONG).show();
                            onSignupFailed();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(signupactivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_EMAIL, email);
               params.put(KEY_PASSWORD, password);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveuserdata() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_EMAIL, signupEmail.getText().toString().trim());
        ed.putString(LOGIN_USERNAME, signupUsername.getText().toString().trim());
        ed.putBoolean(LOGIN_STATE, true);
        ed.putBoolean(FB_LOGIN, false);
        ed.putBoolean(USUAL_LOGIN, true);
        ed.commit();
        Toast.makeText(this, LOGIN_USERNAME, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

       // saveuserdata();
    }




    public void onSignupSuccess() {
       // signupBtn.setEnabled(true);
       // setResult(RESULT_OK, null);
    }

    public void onSignupFailed() {

       // signupBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = signupUsername.getText().toString();
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            signupUsername.setError("at least 3 characters");

            valid = false;
        } else {
            signupUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("enter a valid email address");
            valid = false;
        } else {
            signupEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            signupPassword.setError("between 4 and 15 alphanumeric characters");
            valid = false;
        } else {
            signupPassword.setError(null);
        }

        return valid;
    }

}
