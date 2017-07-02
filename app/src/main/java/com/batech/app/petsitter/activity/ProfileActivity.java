package com.batech.app.petsitter.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Activity;

import com.batech.app.petsitter.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private EditText mTitleField;
    private EditText mBodyField;
    private FloatingActionButton mSubmitButton;

    private String[] ulkeler =
            {"Türkiye", "Almanya", "Avusturya", "Amerika","İngiltere",
                    "Macaristan", "Yunanistan", "Rusya", "Suriye", "İran", "Irak",
                    "Şili", "Brezilya", "Japonya", "Portekiz", "İspanya",
                    "Makedonya", "Ukrayna", "İsviçre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]


            //(A) adımı
            ListView listemiz=(ListView) findViewById(R.id.listView);

            //(B) adımı
            ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, android.R.id.text1, ulkeler);

            //(C) adımı
            listemiz.setAdapter(veriAdaptoru);
    }

}
