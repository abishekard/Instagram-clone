package com.example.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class sharePictureTab extends Fragment implements View.OnClickListener {
    private ImageView imgShare;
    private EditText imgDiscription;
    private Button btnShare;
    private Bitmap recievedImageBitmap;


    public sharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);
        imgShare = view.findViewById(R.id.imgShare);
        imgDiscription = view.findViewById(R.id.imgDiscription);
        btnShare = view.findViewById(R.id.btnShare);
        imgShare.setOnClickListener(sharePictureTab.this);
        btnShare.setOnClickListener(sharePictureTab.this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgShare:
                if(Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},1000
                    );
                }
                else{
                    getChosenImage();
                }
                break;
            case R.id.btnShare: if(recievedImageBitmap != null){
                if(imgDiscription.getText().toString().equals("")){
                    FancyToast.makeText(getContext(),"add description",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                }
                else{
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    recievedImageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    ParseFile parseFile = new ParseFile("img.png",bytes);
                    ParseObject parseObject = new ParseObject("photo");
                    parseObject.put("picture",parseFile);
                    parseObject.put("imgDes",imgDiscription.getText().toString());
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("loading....");
                    progressDialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                FancyToast.makeText(getContext(),"done",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                recievedImageBitmap = null;
                                imgShare.setImageResource(R.drawable.flow1);
                                imgDiscription.setText("");
                            }
                            else {
                                FancyToast.makeText(getContext(),"unknown error",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
            }
               else{
                   FancyToast.makeText(getContext(),"please select an image",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
            }

                break;
        }
    }

    private void getChosenImage() {
       // FancyToast.makeText(getContext(),"image accessible",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000){
            if (resultCode == Activity.RESULT_OK){
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor =getActivity().getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    recievedImageBitmap = BitmapFactory.decodeFile(picturePath);
                    imgShare.setImageBitmap(recievedImageBitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
