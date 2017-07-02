package com.batech.app.petsitter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.batech.app.petsitter.R;
import com.batech.app.petsitter.model.Profiles;
import com.batech.app.petsitter.other.AppController;
import com.batech.app.petsitter.other.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText etxtName;
    private EditText etxtEmail;
    private EditText etxtPassword;
    private Button btnLinkToLoginScreen;
    private Button btnRegister;
    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etxtName = (EditText) findViewById(R.id.name);
        etxtEmail = (EditText) findViewById(R.id.email);
        etxtPassword = (EditText) findViewById(R.id.password);
        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthProgressDialog = new ProgressDialog(RegisterActivity.this);
                mAuthProgressDialog.setTitle("Loading");
                mAuthProgressDialog.setMessage("Creating user...");
                mAuthProgressDialog.setCancelable(false);
                mAuthProgressDialog.show();
                final String name = etxtName.getText().toString().trim();
                final String email = etxtEmail.getText().toString().trim();
                String password = etxtPassword.getText().toString().trim();

                mAuth = FirebaseAuth.getInstance();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // Staring MainActivity
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.sendEmailVerification();
                                    createUserToDatabase(user.getUid(), name, name, email);
                                    Intent i = new Intent(getApplicationContext(), com.batech.app.petsitter.activity.LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                                mAuthProgressDialog.hide();
                            }
                        });
            }
        });
        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    public boolean createUserToDatabase(final String userId, final String name, final String surname, final String emailAddress){
        // making fresh volley request and getting json

        Profiles profiles = new Profiles(name, surname, userId, "");

        Map<String, Object> profileValues = profiles.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
//            childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/users/" + userId, profileValues);

        mDatabase.updateChildren(childUpdates);

//        JSONObject jsonObj = new JSONObject(params);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATEUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "createuserdatabase:response:"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "createuserdatabase:response:"+error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("createuser", "1");
                params.put("name", name);
                params.put("surname", surname);
                params.put("emailaddress", emailAddress);
                params.put("userid", userId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
/*
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
                Constants.URL_CREATEUSER, jsonObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
//                    parseJsonFeed(response);

                    Log.d(TAG, "createuserdatabase:response:"+response.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
*/
        return true;
    }
}
