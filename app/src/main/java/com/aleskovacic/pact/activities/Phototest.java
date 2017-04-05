package com.aleskovacic.pact.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aleskovacic.pact.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


// сделано по уроку https://www.simplifiedcoding.net/android-volley-tutorial-to-upload-image-to-server/

public class Phototest extends AppCompatActivity implements View.OnClickListener {


    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;


    private Bitmap bitmap;
    public Bitmap resizedimg;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://nearme.s-host.net/RegisterFB/uploadphoto.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    String TAG = "Uploadphoto";
    String idofuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phototest);


        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);


        imageView  = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        Intent editphoto = getIntent();

        idofuser = editphoto.getStringExtra("userid");

    }


//выбираем откуда берем фото (галерея, гугл фото) и выбираем фото
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

//если выбрали фото, то присваиваем его imageView

    // имеет смысл попробовать этот ответ http://stackoverflow.com/questions/25490928/androidselect-image-from-gallery-then-crop-that-and-show-in-an-imageview
    // важный коммент к предыдущей ссылке коммент - both these activities must have android:launchMode="singleTop" and android:noHistory="false" attributes in the manifest. Otherwise it doesn't work and onActivityResult gets called with RESULT_CANCELED. It took me hours to figure it out. – Atul May 3 '16 at 11:07
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();


            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);


                //Setting the Bitmap to ImageView
              imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
// переводим Image в String
       public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e(TAG, encodedImage);
        return encodedImage;

    }

    //загружаем фото на сервер
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e(TAG, " on response " + s);

                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Phototest.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.e(TAG, " on error " + volleyError);

                        //Showing toast
                        Toast.makeText(Phototest.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                String image = getStringImage(bitmap);
                Log.e(TAG, " string of image " + image);
                String name = idofuser;
                //Getting Image Name
             //   String name = editTextName.getText().toString().trim();
                Log.e(TAG, " name of photo" + name);


                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
            showFileChooser();
        }

        if(v == buttonUpload){
            uploadImage();
        }
    }
}
