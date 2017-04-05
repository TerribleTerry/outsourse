package com.aleskovacic.pact.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aleskovacic.pact.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alex on 08.01.2017.
 */

public class RecoveryFragment extends Fragment {
    private ProgressDialog pDialog;

    Button mContinueBtn;
    EditText mEmail;
    public String email;
    private static final String RECOVERY_URL = "http://nearme.s-host.net/RegisterFB/recovery.php";


    public RecoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recovery, container, false);


        mEmail = (EditText) rootView.findViewById(R.id.PasswordRecoveryEmail);

        mContinueBtn = (Button) rootView.findViewById(R.id.PasswordRecoveryBtn);

        mContinueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 email = mEmail.getText().toString();


                    Helper helper = new Helper();

                    if (helper.isValidEmail(email)) {

                        recovery();

                    } else {

                        Toast.makeText(getActivity(), getText(R.string.error_email), Toast.LENGTH_SHORT).show();
                    }

            }




        });


        // Inflate the layout for this fragment
        return rootView;
    }

    public void onDestroyView() {

        super.onDestroyView();

        hidepDialog();
    }


    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing()) pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing()) pDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void recovery() {


        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECOVERY_URL,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                        String answer = response.toString();
                        if (answer.equals("fail")) {
                            Toast.makeText(getContext(), "Неправильна пошта або пароль, спробуйте ще", Toast.LENGTH_LONG).show();


                        } else if(answer.equals("loged")){
                            Toast.makeText(getContext(), "Привіт", Toast.LENGTH_LONG).show();


                        }else{
                            Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            Log.e("recovery error", response);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

                }


















    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
