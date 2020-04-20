package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class userPosts extends AppCompatActivity {
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        linearLayout = findViewById(R.id.linearLayout);
        Intent recievedIntentObject = getIntent();
        String recievedUsername = recievedIntentObject.getStringExtra("username");
        setTitle(recievedUsername + "'s post");
        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("photo");
        parseQuery.whereEqualTo("username",recievedUsername);
        parseQuery.orderByDescending("createdAt");
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size() > 0 && e == null){
                    for(ParseObject post : objects){
                        final TextView postDescription = new TextView(userPosts.this);
                        postDescription.setText(post.get("imgDes")+ "");
                        final ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data != null && e == null){
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(userPosts.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(5,5,5,5);
                                    postImageView.setLayoutParams(params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams desParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    desParams.setMargins(5,5,5,5);
                                     postDescription.setLayoutParams(desParams);
                                     postDescription.setGravity(Gravity.CENTER);
                                     postDescription.setBackgroundColor(Color.BLUE);
                                     postDescription.setTextColor(Color.WHITE);
                                     postDescription.setTextSize(30f);
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);
                                }
                            }
                        });

                    }
                    dialog.dismiss();
                }
                else {
                    FancyToast.makeText(userPosts.this,"don't have any post",FancyToast.INFO,FancyToast.LENGTH_LONG,false).show();
                    finish();
                }
            }
        });
    }
}
