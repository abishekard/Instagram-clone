package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLog,btnSign;
    private EditText txtEmail,txtPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Log in");
        btnLog = findViewById(R.id.btnLog);
        btnSign = findViewById(R.id.btnSign);
        txtEmail = findViewById(R.id.txtEmail);
        txtPsd = findViewById(R.id.txtPsd);
        txtPsd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                onClick(btnLog);
                return false;
            }
        });
        btnSign.setOnClickListener(signUpActivity.this);
        btnLog.setOnClickListener(signUpActivity.this);
        if(ParseUser.getCurrentUser() != null){
            //ParseUser.getCurrentUser().logOut();
            transitionActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnSign:  finish();
                break;
            case R.id.btnLog:
                if(txtEmail.getText().toString().equals("")||txtPsd.getText().toString().equals("")){
                    FancyToast.makeText(signUpActivity.this,"no fields can be left empty",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show();
                }
                else{
                ParseUser.logInInBackground(txtEmail.getText().toString(), txtPsd.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null && e == null){
                            FancyToast.makeText(signUpActivity.this,"login successfull",FancyToast.SUCCESS,FancyToast.LENGTH_LONG,true).show();
                            transitionActivity();
                        }
                        else{
                        FancyToast.makeText(signUpActivity.this,e.getMessage(),FancyToast.ERROR,FancyToast.LENGTH_LONG,true).show();}
                    }
                });
                break;
        }

        }


    }
    public void layoutTap(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void transitionActivity(){
        Intent intent = new Intent(signUpActivity.this,socialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}
