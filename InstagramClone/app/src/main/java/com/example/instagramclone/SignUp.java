package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.security.PrivateKey;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

     Button btnSignUp,btnLogIn;
     EditText txt1,txt2,txt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    onClick(btnSignUp);
                return false;
            }
        });
        btnLogIn.setOnClickListener(SignUp.this);
        btnSignUp.setOnClickListener(SignUp.this);
        if(ParseUser.getCurrentUser() != null){
           // ParseUser.getCurrentUser().logOut();
            transitionActivity();
        }
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnLogIn: Intent intent = new Intent(SignUp.this,signUpActivity.class);
                                startActivity(intent);
                                break;
            case R.id.btnSignUp:
                 if(txt1.getText().toString().equals("") || txt2.getText().toString().equals("")|| txt3.getText().toString().equals("")){
                     FancyToast.makeText(SignUp.this,"No fields can be left empty",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                 }
                 else{
                ParseUser appUser = new ParseUser();
                 appUser.setEmail(txt1.getText().toString());
                 appUser.setUsername(txt2.getText().toString());
                 appUser.setPassword(txt3.getText().toString());
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing Up  " + txt2.getText().toString());
                progressDialog.show();
                 appUser.signUpInBackground(new SignUpCallback() {
                     @Override
                     public void done(ParseException e) {
                         if(e == null){
                             FancyToast.makeText(SignUp.this,"account created",FancyToast.SUCCESS,FancyToast.LENGTH_LONG,true).show();
                             transitionActivity();
                             progressDialog.dismiss();
                         }
                         else{
                             FancyToast.makeText(SignUp.this,e.getMessage(),FancyToast.ERROR,FancyToast.LENGTH_LONG,true).show();
                             progressDialog.dismiss();
                         }
                     }
                 });
                                break;
        }}

    }
    public  void relativeLayoutTap(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void transitionActivity(){
        Intent intent = new Intent(SignUp.this,socialMediaActivity.class);
        startActivity(intent);
        finish();
    }

}

