package com.aleskovacic.pact.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aleskovacic.pact.R;
import com.aleskovacic.pact.pojo.Comment;
import com.aleskovacic.pact.pojo.CommentsResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MessageChatActivity extends AppCompatActivity {


    Date date;
    LinearLayout linLayout;
    private static final String TAG = "MessageLayout";
    ImageView photo;
    LinearLayout mCommentFormContainer;
    String commentText;
    EditText mCommentText;
    public String item_text = "";
    public String item_username = "";
    public String item_date = "";
    public int intValue;
    public String replytoUserid, fromUserId, replyonpostId;
    ImageView mSendComment;
    public String mainpostID = "";
    public String FromUserId = "";
    public String commenttime = " ";
    public String email = " ";
    SharedPreferences sPref;
    public String applytext;
    final String SAVED_EMAIL = "saved_email";
    private static final String POSTCOMMENT_URL = "http://nearme.s-host.net/RegisterFB/postcomment_2_inserts.php";
    private static final String ABUSEREPORT = "http://nearme.s-host.net/RegisterFB/post_abuse_report.php";

    public static final String KEY_COMMENTTEXT = "commentText";
    public static final String KEY_EMAIL = "email";  //TO FIND USER ID IN SIGNUP DB
    public static final String KEY_TIME = "commenttime";
    public static final String KEY_FBID = "fb_id";
    String fb_id;


    public static final String KEY_REPLYTOUSERID = "replytoUserid";
    public static final String KEY_REPLYONPOSTID = "replyonpostId";
    private AppBarLayout mAppBarLayout;


    LinearLayout mContentScreen, mItemLocationContainer;

    public static String postid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            ab.setTitle("Chat");

        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));


        FacebookSdk.sdkInitialize(getApplicationContext());

        //
         mCommentFormContainer = (LinearLayout) findViewById(R.id.commentFormContainer);
        mCommentText = (EditText) findViewById(R.id.commentText);


        mContentScreen = (LinearLayout) findViewById(R.id.contentScreen);

        final TextView textofmessage = (TextView) findViewById(R.id.textofmessage);
        final TextView timeofmessage = (TextView) findViewById(R.id.timeofmessage);
        final TextView fromuser = (TextView) findViewById(R.id.fromuser);
        mSendComment = (ImageView) findViewById(R.id.sendCommentImg);

        mSendComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                send();
            }
        });

            Intent intent = getIntent();
        intValue = intent.getIntExtra("mesnumber", 0);

        Log.i(TAG, "результат" + intValue);



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Get get = Get.retrofit.create(Get.class);

        Call<MessageResponse> call = get.getResult();
        call.enqueue(new Callback<MessageResponse>() {


            @Override
            public void onResponse(Response<MessageResponse> response, Retrofit retrofit) {
                ArrayList<MessageResult> arrayMessageList = response.body().getResult();
                item_text = arrayMessageList.get(intValue).getMtext();
                textofmessage.setText(item_text);
                registerForContextMenu(textofmessage);
                Log.i(TAG, "результат в ретрофите" + item_text);
                item_username = arrayMessageList.get(intValue).getLastname();
                fromuser.setText(item_username);
                Log.i(TAG, "результат в ретрофите" + item_username);
                item_date = arrayMessageList.get(intValue).getMessagetime().toString();

             //   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //    try {
              //       date = sdf.parse(item_date);
              //  } catch (ParseException e) {
              //      e.printStackTrace();
              //  }
             //   sdf = new SimpleDateFormat();
             ///  String  newdate = sdf.format(date);
             //   System.out.println( sdf.format(date) );
//Log.e("new date format: ", newdate);


                timeofmessage.setText(item_date);
                mainpostID = arrayMessageList.get(intValue).getId();
                FromUserId = arrayMessageList.get(intValue).getFromUserId();
                Log.i(TAG, "результат в ретрофите" + "айди поста: " + mainpostID);
                fetchcomments(mainpostID);
            }



            @Override
            public void onFailure(Throwable t) {
                textofmessage.setText("Something went wrong: " + t.getMessage());

            }
        });





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


    private void fetchcomments(String mainpostID) {
        Log.i(TAG, "результат в фетче" + "айди поста: " + mainpostID);

        final LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        registerForContextMenu(linLayout);

        final LayoutInflater ltInflater = getLayoutInflater();

        Get get = Get.retrofit.create(Get.class);


        replyonpostId = mainpostID;

        Log.e("replyonpostId", replyonpostId);

        Call<CommentsResponse> callcomment = get.getComments(replyonpostId);
        callcomment.enqueue(new Callback<CommentsResponse>() {




            @Override
            public void onResponse(Response<CommentsResponse> response, Retrofit retrofit) {
                List<Comment> arraycomment = response.body().getComments();
                for(int i = 0; i < arraycomment.size(); i++){
                    View item = ltInflater.inflate(R.layout.chat_item, linLayout, false);


                    TextView commTextView = (TextView) item.findViewById(R.id.commTextView);
                    commTextView.setText(arraycomment.get(i).getCommentText());
                    TextView commNameView = (TextView) item.findViewById(R.id.commNameView);
                    commNameView.setText(arraycomment.get(i).getUsername());
                    TextView commtTimeView = (TextView) item.findViewById(R.id.commtTimeView);
                    commtTimeView.setText(arraycomment.get(i).getCommenttime());
                    item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                    linLayout.addView(item);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Something went wrong: ", t.getMessage());

            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.apply:

                sendabuse();

                Log.e("ListItem position", applytext);// метод, выполняющий действие при редактировании пункта меню
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void sendabuse() {
        applytext = replyonpostId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ABUSEREPORT,
                new com.android.volley.Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MessageChatActivity.this, response, Toast.LENGTH_LONG).show();

                        Log.e("volley", response);
                    }

                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volleyresponse: ", error.toString());
                        Toast.makeText(MessageChatActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("replyonpostId", replyonpostId);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    };




    private void send() {

        sPref = this.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String email = sPref.getString(SAVED_EMAIL, "");



        Profile profile = Profile.getCurrentProfile().getCurrentProfile();
        if(profile != null) {
             fb_id = profile.getId();
        }


        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        commentText = mCommentText.getText().toString();
        commentText = commentText.trim();
        commenttime = sdfDate.format(now);







        replytoUserid = FromUserId;
        replyonpostId = mainpostID;
        Log.e(TAG, "Текст сообщения" + commentText  + "replyonpostId: " + replyonpostId);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, POSTCOMMENT_URL,
                new com.android.volley.Response.Listener<String>(){


                    @Override
                    public void onResponse(String response) {

                       Toast.makeText(MessageChatActivity.this, response, Toast.LENGTH_LONG).show();
                        Log.e("volleyresponse: ", response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MessageChatActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("volleyresponse: ", error.toString());

                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_COMMENTTEXT, commentText);
               params.put(KEY_EMAIL, email);
                params.put(KEY_TIME, commenttime);
               params.put(KEY_REPLYTOUSERID, replytoUserid);
               params.put(KEY_FBID, fb_id);
               params.put(KEY_REPLYONPOSTID, replyonpostId);
                Log.e(TAG, "Текст передаваемій" + commentText  + "replyonpostId: " + replyonpostId + "email: " + email + "fb_id = " + fb_id);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
