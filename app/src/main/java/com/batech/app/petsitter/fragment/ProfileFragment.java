package com.batech.app.petsitter.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.batech.app.petsitter.R;
import com.batech.app.petsitter.model.Profiles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import com.batech.app.petsitter.helper.Helper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View myFragmentView;
    private Button savebtn;
    private EditText p_name;
    private EditText p_surname;
    private EditText p_username;
    private EditText p_email;
    private EditText p_bday;
    private EditText p_contactNumber;

    private DatabaseReference mDatabase;

    private ProgressBar mProgressBar;

    FirebaseUser user;
    private String user_id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myFragmentView = inflater.inflate(R.layout.fragment_profiles, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_id = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        p_name = (EditText) myFragmentView.findViewById(R.id.name);
        p_surname = (EditText) myFragmentView.findViewById(R.id.surname);
        p_username = (EditText) myFragmentView.findViewById(R.id.username);
        p_bday = (EditText) myFragmentView.findViewById(R.id.birthday);
        p_contactNumber = (EditText) myFragmentView.findViewById(R.id.contactnumber);

        mProgressBar = (ProgressBar) myFragmentView.findViewById(R.id.progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        mDatabase.child("users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profiles profile = dataSnapshot.getValue(Profiles.class);
                p_name.setText(profile.getName());
                p_surname.setText(profile.getSurname());
                p_username.setText(profile.getUsername());
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        savebtn = (Button) myFragmentView.findViewById(R.id.saveBtn);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(user.getUid(),p_name.getText().toString(),p_surname.getText().toString(),p_username.getText().toString());

                Toast.makeText(getActivity(), R.string.updatedSuccessfully,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateUI(View view, FirebaseUser user) {
        if (user != null) {
            ((TextView) view.findViewById(R.id.name)).setText(mDatabase.child("users").child(user_id).child("name").getKey());
        } else {
            ((TextView) view.findViewById(R.id.name)).setText("Error: update failed.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView) myFragmentView.findViewById(R.id.name)).setText(user.getDisplayName());
    }
    public void updateProfile(String userId, String name, String surname, String username){
            // Create new post at /user-posts/$userid/$postid and at
            // /posts/$postid simultaneously
//            String key = mDatabase.child("users").push().getKey();
            Profiles profiles = new Profiles(name, surname, userId, username);
            Map<String, Object> profileValues = profiles.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
//            childUpdates.put("/posts/" + key, postValues);
            childUpdates.put("/users/" + userId, profileValues);

            mDatabase.updateChildren(childUpdates);
    }
}
