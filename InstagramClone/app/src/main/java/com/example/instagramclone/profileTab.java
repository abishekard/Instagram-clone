package com.example.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileTab extends Fragment {
    private EditText txtPfName,txtBio,txtProf,txtHobbies,txtFvSports;
    private Button infoUpdate;



    public profileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        txtPfName =view.findViewById(R.id.txtPfName);
        txtBio = view.findViewById(R.id.txtBio);
        txtProf = view.findViewById(R.id.txtProf);
        txtHobbies = view.findViewById(R.id.txtHobbies);
        txtFvSports =view.findViewById(R.id.txtFvSports);
        infoUpdate = view.findViewById(R.id.infoUpdate);
        final ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser.get("profileName") == null){
            txtPfName.setText("");
        }
        else if(parseUser.get("profileBio")== null){
            txtBio.setText("");
        }
        else if(parseUser.get("profileProffesion") == null){
            txtProf.setText("");
        }
        else if(parseUser.get("profileHobbies")==null){
            txtHobbies.setText("");
        }
        else if(parseUser.get("profileFavsports") == null){
            txtProf.setText("");
        }
        else{
        txtPfName.setText(parseUser.get("profileName") + "");
        txtBio.setText(parseUser.get("profileBio") + "");
        txtProf.setText(parseUser.get("profileProffesion")+"");
        txtHobbies.setText(parseUser.get("profileHobbies")+"");
            txtFvSports.setText(parseUser.get("profileFavsports")+"");}
        infoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("updating");
                progressDialog.show();
                parseUser.put("profileName",txtPfName.getText().toString());
                parseUser.put("profileBio",txtBio.getText().toString());
                parseUser.put("profileProffesion",txtProf.getText().toString());
                parseUser.put("profileHobbies",txtHobbies.getText().toString());
                parseUser.put("profileFavsports",txtFvSports.getText().toString());
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(getContext(),"info updated",FancyToast.INFO,FancyToast.LENGTH_LONG,true).show();
                            progressDialog.dismiss();
                        }
                        else{
                            FancyToast.makeText(getContext(),e.getMessage(),FancyToast.ERROR,FancyToast.LENGTH_LONG,true).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
        return view;

    }

    }


