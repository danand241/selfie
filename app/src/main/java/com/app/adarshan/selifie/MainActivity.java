package com.app.adarshan.selifie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.ByteArrayOutputStream;

import mehdi.sakout.fancybuttons.FancyButton;


public class MainActivity extends AppCompatActivity
{
    public static final int CAMERA_REQUEST = 10;
    public static int TIMES = 1;
    private FancyButton takePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        takePicture = (FancyButton) findViewById(R.id.take_image);

        takePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                takeImage(v);
            }
        });
       /* Button button = (Button) findViewById(R.id.save);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
        final ImageView imageView = (ImageView) findViewById(R.id.save_image);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageView.setImageBitmap(viewToBitmap(relativeLayout));
            }
        });*/
    }

    private void takeImage(View v)
    {
        UserLocalStore userLocalStore = new UserLocalStore(this);
        userLocalStore.setPictures(TIMES);
        if(userLocalStore.getTimes() <= 2) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            TIMES++;

           /* Bitmap first = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.first);
            databaseHelper.insert(getBytes(first));
            Bitmap second = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.second);
            databaseHelper.insert(getBytes(second));*/
        }
        else {
            startActivity(new Intent(this, ImageActivity.class));
            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {
                Bitmap camerImage = (Bitmap) data.getExtras().get("data");
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.insert(getBytes(camerImage));
            }

        }
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
