package com.batech.app.petsitter.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.android.gms.ads.MobileAds;

public class AllPostsFragment extends PostListFragment {

    public AllPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("posts");
    }
}
