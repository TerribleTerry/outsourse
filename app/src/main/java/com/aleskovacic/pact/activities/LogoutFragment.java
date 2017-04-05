package com.aleskovacic.pact.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aleskovacic.pact.R;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Alex on 15.11.2016.
 */

public class LogoutFragment extends Fragment implements View.OnClickListener{


    SharedPreferences sPref;
    final String SAVED_EMAIL = "saved_email";
    final String LOGIN_USERNAME = "login_state";
    final String LOGIN_STATE = "login_state";

    Button logoutbtn;


    Fragment fragmentlogout;
    Fragment fragmentsignin;
    Fragment Fblogingragment;
private boolean wheatherlogged;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.logoutfragment, container, false);
        logoutbtn = (Button) rootView.findViewById(R.id.logoutbtn);
        wheatherlogged = false;
        logoutbtn.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if(v == logoutbtn){
            wheatherlogged = true;
            logoutUser();
        }
    }

    private void logoutUser() {
        if(wheatherlogged) {
            sPref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(SAVED_EMAIL, null);
            ed.putBoolean(LOGIN_STATE, false);
            ed.putString(LOGIN_USERNAME, null);
            ed.commit();
            onLogoutSuccess();
        }else{
            Toast.makeText(getContext(), "No action", Toast.LENGTH_SHORT).show();

        }
    }
    public void onLogoutSuccess() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.progress_bar_dialog);
        getActivity().finish();
        progressDialog.dismiss();
        Toast.makeText(getContext(), "You logged out", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        logoutUser();
    }

}
