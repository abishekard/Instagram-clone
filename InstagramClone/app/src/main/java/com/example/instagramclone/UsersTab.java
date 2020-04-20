package com.example.instagramclone;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener {
    private ListView lview;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private ProgressDialog progressDialog;


    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_tab, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();
        lview = view.findViewById(R.id.lview);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        lview.setOnItemClickListener(UsersTab.this);
        lview.setOnItemLongClickListener(UsersTab.this);
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("useraname",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseUser user : objects){
                            arrayList.add(user.getUsername());
                        }
                        lview.setAdapter(arrayAdapter);
                        progressDialog.dismiss();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(),userPosts.class);
        intent.putExtra("username",arrayList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
      parseQuery.whereEqualTo("username",arrayList.get(position));
      final PrettyDialog prettyDialog = new PrettyDialog(getContext());
      parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
          @Override
          public void done(ParseUser object, ParseException e) {
              if(object != null && e == null){
                  prettyDialog.setTitle(object.getUsername()+"'s info").setMessage("name:"+object.get("profileName: ")+"\n" + "profession: " +object.get("profileProffesion")
                          +"\n"+"favourite sports: "+object.get("profileFavsports")+"\n"+"Hobbies: "+object.get("profileHobbies")+"\n" +"Bio: "+ object.get("profileBio")).setIcon(R.drawable.person).
                          addButton("ok", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                              @Override
                              public void onClick() {
                                  prettyDialog.dismiss();
                              }
                          }).show();

              }
          }
      });
        return true;
    }
}
