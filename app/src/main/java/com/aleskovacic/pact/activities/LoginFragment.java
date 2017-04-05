package com.aleskovacic.pact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aleskovacic.pact.R;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by Alex on 15.11.2016.
 */

public class LoginFragment extends Fragment implements View.OnClickListener{




    private static final String LOGINUSER_URL = "http://nearme.s-host.net/RegisterFB/logincheck.php";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    final String FB_LOGIN = "fb_login_state";
    final String USUAL_LOGIN = "usual_login_state";

    private TextView loginBtn;
    Button sendLogin;
    Button signUp;
    private TextView forgotPassword, Emailhint, Passwordhint;
    private EditText loginPassword, loginemail;

    SharedPreferences sPref;
    private static int REQUEST_LOGIN = 0;
    public boolean wheaterlogged;
    final String SAVED_EMAIL = "saved_email";
     String LOGIN_STATE = "login_state";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_login, container, false);




        loginPassword = (EditText) rootView.findViewById(R.id.input_password);
        loginemail = (EditText) rootView.findViewById(R.id.input_email);
        forgotPassword = (TextView) rootView.findViewById(R.id.forgotPassword);
        Emailhint = (TextView) rootView.findViewById(R.id.forgotPassword);
        Passwordhint = (TextView) rootView.findViewById(R.id.forgotPassword);
        sendLogin = (Button) rootView.findViewById(R.id.sendlogin);
        signUp = (Button) rootView.findViewById(R.id.signup);
        sendLogin.setOnClickListener(this);
        signUp.setOnClickListener(this);

        //forgotPassword.setVisibility(View.GONE);
        forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), RecoveryActivity.class);
                startActivity(i);
            }
        });

      //  loginBtn = (TextView) rootView.findViewById(R.id.btn_login);
       // loginBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
       if(v == sendLogin){
          loginUser();
       }else if(v == signUp){
           Intent sigupuser = new Intent(getActivity(), signupactivity.class);
           startActivityForResult(sigupuser, 1);
       }
    }




    private void loginUser() {

        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
           // forgotPassword.setVisibility(View.VISIBLE);
            return;
        }
//loginBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.progress_bar_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        final String email = loginemail.getText().toString().trim();
        final String password = loginPassword.getText().toString().trim();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGINUSER_URL,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                           //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        String answer = response.toString();
                        if (answer.equals("fail")) {
                            onLoginFailed();

                            //forgotPassword.setVisibility(View.VISIBLE);


                        } else if(answer.equals("loged")){
                            wheaterlogged = true;
                            savestate();
                            onLoginSuccess();
                        }else{
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                          //  forgotPassword.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        forgotPassword.setVisibility(View.VISIBLE);

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    public void savestate() {
        if(wheaterlogged){
        sPref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_EMAIL, loginemail.getText().toString().trim());

        ed.putBoolean(LOGIN_STATE, true);
        ed.putBoolean(FB_LOGIN, false);
        ed.putBoolean(USUAL_LOGIN, true);
        ed.commit();
        Toast.makeText(getContext(), "Text saved", Toast.LENGTH_SHORT).show();
        }else{
            sPref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();

            ed.putBoolean(LOGIN_STATE, false);
            ed.putBoolean(FB_LOGIN, false);
            ed.commit();
            Toast.makeText(getContext(), "Nothing in login", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        savestate();
    }
    public void onLoginFailed() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.progress_bar_dialog);
        progressDialog.dismiss();
        Toast.makeText(getContext(), "Неправильна пошта або пароль, спробуйте ще", Toast.LENGTH_LONG).show();

    }



    public void onLoginSuccess() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.progress_bar_dialog);
getActivity().finish();
        progressDialog.dismiss();
        Toast.makeText(getContext(), "Привіт", Toast.LENGTH_LONG).show();
    }


    public boolean validate() {
        boolean valid = true;

        String email = loginemail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginemail.setError("enter a valid email address");
            forgotPassword.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            loginemail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            loginPassword.setError("between 4 and 15 alphanumeric characters");
            forgotPassword.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            loginPassword.setError(null);
        }

        return valid;
    }


}
