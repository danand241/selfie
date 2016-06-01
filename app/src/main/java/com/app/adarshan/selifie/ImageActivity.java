package com.app.adarshan.selifie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adarshan on 5/26/16.
 */
public class ImageActivity extends AppCompatActivity
{
    private ShareButton shareButton;
    private CallbackManager callbackManager;
    private ImageView first, second;
    private DatabaseHelper databaseHelper;
    private List<Bitmap> bitmapList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.test);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);

        callbackManager = CallbackManager.Factory.create();
        shareButton = (ShareButton) findViewById(R.id.shareButton);

        first = (ImageView) findViewById(R.id.first);
        second = (ImageView) findViewById(R.id.second);

        bitmapList = new ArrayList<Bitmap>();
        databaseHelper = new DatabaseHelper(this);

        bitmapList = databaseHelper.getImage();
       /* databaseHelper.deleteAll();*/
        first.setImageBitmap(bitmapList.get(0));
        second.setImageBitmap(bitmapList.get(1));

        SharePhoto sharePhoto1 = new SharePhoto.Builder()
            .setBitmap(getBitmapFromView(relativeLayout))
            .build();
        ShareContent shareContent = new ShareMediaContent.Builder()
            .addMedium(sharePhoto1).build();

        shareButton.setShareContent(shareContent);
        shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>()
        {
            @Override
            public void onSuccess(Sharer.Result result)
            {
                databaseHelper.deleteAll();
                UserLocalStore userLocalStore = new UserLocalStore(ImageActivity.this);
                userLocalStore.clearUserData();
            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException error)
            {

            }
        });
        /*shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharePhoto sharePhoto1 = new SharePhoto.Builder()
                    .setBitmap(viewToBitmap(relativeLayout))
                    .build();
                ShareContent shareContent = new ShareMediaContent.Builder()
                    .addMedium(sharePhoto1).build();
                ShareDialog shareDialog = new ShareDialog(ImageActivity.this);
                shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
            }
        });*/

    }

    /*private Bitmap viewToBitmap(View view)
    {
        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

   /* protected Bitmap convertToBitmap(View  layout) {

        Bitmap viewCapture = null;

        layout.setDrawingCacheEnabled(true);

        viewCapture = Bitmap.createBitmap(layout.getDrawingCache());

        layout.setDrawingCacheEnabled(false);

        return viewCapture;
    }
*/
    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }
}
