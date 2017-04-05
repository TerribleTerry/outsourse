package com.aleskovacic.pact.activities;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;


import com.aleskovacic.pact.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;



import java.util.HashMap;
import java.util.Map;


import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ContactUs extends AppCompatActivity implements View.OnClickListener {


    private AppBarLayout mAppBarLayout;


Button sendcontact;
    private ProgressDialog pDialog;

    private String email, subject, detail;

    EditText mEmail, mSubject, mDetail;
    TextView contactfree;
    private Boolean loading = false;
    SharedPreferences sPref;

    final String SAVED_EMAIL = "saved_email";

    private static final String POSTCONTACTUS = "http://nearme.s-host.net/RegisterFB/contactus.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(R.string.connect);

        }

        initpDialog();





        mEmail = (EditText) findViewById(R.id.email);
        mSubject = (EditText) findViewById(R.id.subject);
        mDetail = (EditText) findViewById(R.id.detail);

        contactfree = (TextView) findViewById(R.id.contact);

    }


    protected void initpDialog() {

        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
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

                email = mEmail.getText().toString();
                subject = mSubject.getText().toString();
                detail = mDetail.getText().toString();

                if (email.length() > 0 && subject.length() > 0 && detail.length() > 0){

                    sendTicket();

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




    public void onDestroy() {

        super.onDestroy();

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }




    private void sendTicket() {



        sPref = this.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String loginemail = sPref.getString(SAVED_EMAIL, "");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, POSTCONTACTUS,
                new com.android.volley.Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ContactUs.this, response, Toast.LENGTH_LONG).show();

                        Log.e("volley", response);
                    }

                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volleyresponse: ", error.toString());
                        Toast.makeText(ContactUs.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("subject", subject);
                params.put("detail", detail);
                params.put("loginemail", loginemail);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        };

    @Override
    public void onClick(View view) {


        email = mEmail.getText().toString();
        subject = mSubject.getText().toString();
        detail = mDetail.getText().toString();

        if (email.length() > 0 && subject.length() > 0 && detail.length() > 0){

            sendTicket();

        } else {

            Toast.makeText(getApplicationContext(), getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
        }


    }


};










