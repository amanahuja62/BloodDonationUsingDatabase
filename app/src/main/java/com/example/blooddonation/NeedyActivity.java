package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NeedyActivity extends AppCompatActivity {


    String donorBloodGroup;
    RecyclerView recyclerView;
    DatabaseReference databaseAcceptors;
    MyAdapter myAdapter;
    List<Acceptor> acceptorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needy);
        recyclerView = findViewById(R.id.recyid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorBloodGroup=getIntent().getStringExtra("donorBloodGroup");
        databaseAcceptors = FirebaseDatabase.getInstance().getReference("AcceptorInfo");
        myAdapter = new MyAdapter();
        acceptorList = new ArrayList<>();
        recyclerView.setAdapter(myAdapter);
        databaseAcceptors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                acceptorList.clear();
                 for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                     String name =  dataSnapshot.child("name").getValue(String.class);
                     String imageURL =  dataSnapshot.child("imageURL").getValue(String.class);
                     String id =  dataSnapshot.child("id").getValue(String.class);
                     String mobile =  dataSnapshot.child("mobile").getValue(String.class);
                     String bloodGroup =  dataSnapshot.child("bloodGroup").getValue(String.class);
                     String address =  dataSnapshot.child("address").getValue(String.class);
                     System.out.println(name);
                     Log.d("asaas",bloodGroup);

                     if(bloodGroup.equals(donorBloodGroup) )
                     acceptorList.add(new Acceptor( imageURL,  name,  address,  mobile,  bloodGroup,  id));

                 }
                myAdapter.setData(acceptorList);
            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(NeedyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}