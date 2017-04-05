package com.aleskovacic.pact.activities;

/**
 * Created by Alex on 03.12.2016.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.aleskovacic.pact.R;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class Message extends AppCompatActivity {
    private static final String TAG = "MyApp";
    public ArrayList<MessageResult> result;
    public MessageResponse response;
    public int intValue;

    public String item_text = "";
    public String item_username = "";
    public String item_date = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final TextView message_text = (TextView) findViewById(R.id.message_text);
        final TextView message_time = (TextView) findViewById(R.id.message_time);
        final  TextView user_lastname = (TextView) findViewById(R.id.user_lastname);


        Intent intent = getIntent();
        intValue = intent.getIntExtra("mesnumber", 0);
        Log.i(TAG, "результат" + intValue);


        Get get = Get.retrofit.create(Get.class);


        Call<MessageResponse> call = get.getResult();
        call.enqueue(new Callback<MessageResponse>() {


            @Override
            public void onResponse(Response<MessageResponse> response, Retrofit retrofit) {
                ArrayList<MessageResult> arrayMessageList = response.body().getResult();
                item_text = arrayMessageList.get(intValue).getMtext();
                message_text.setText(item_text);
                Log.i(TAG, "результат в ретрофите" + item_text);
                item_username = arrayMessageList.get(intValue).getLastname();
                user_lastname.setText(item_username);
                Log.i(TAG, "результат в ретрофите" + item_username);
                item_date = arrayMessageList.get(intValue).getMessagetime().toString();
                message_time.setText(item_date);
                Log.i(TAG, "результат в ретрофите" + item_date);
            }

            @Override
            public void onFailure(Throwable t) {
                message_text.setText("Something went wrong: " + t.getMessage());
            }

        });


        //     message_text.setText(item_text);
        // Log.i(TAG, "результат в поле" + item_text);
        //  user_lastname.setText(item_username);
        // Log.i(TAG, "результат в поле" + user_lastname);
        message_time.setText(item_date);
        // Log.i(TAG, "результат в поле" + message_time);
    }
}
