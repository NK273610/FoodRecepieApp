package com.example.nikhildhirmalani.assignment4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nikhildhirmalani on 23/03/18.
 */

public class FireBase_Fetcher implements Serializable {
    public static final String FIREBASE_CHILD_PRODUCTS = "recipes";
    int count;
    List<Data_Class>list;
    CustomAdapter cv;

    public void FireBase_Data(final RecyclerView rv) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FIREBASE_CHILD_PRODUCTS);




        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String,Data_Class>> genericTypeIndicator =new GenericTypeIndicator<Map<String,Data_Class>>(){};
                final Map<String,Data_Class> obj;

                obj = (Map<String,Data_Class>) dataSnapshot.getValue(genericTypeIndicator);
                list = new ArrayList<Data_Class>(obj.values());
                cv=new CustomAdapter(list);
                rv.setAdapter(cv);
                count = obj.size();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
