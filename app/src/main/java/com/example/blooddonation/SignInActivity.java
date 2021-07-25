package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    EditText e1;
    String bloodGroup;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        e1 = findViewById(R.id.editTextTextPersonName2);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("DonorInfo"); // will be used for storing donor details

    }

    public void signinmethod(View view) {

        Query query = databaseReference.orderByChild("mobileNo").equalTo(e1.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for( DataSnapshot dataSnapshot : snapshot.getChildren() ) {
                        if(dataSnapshot.child("mobileNo").getValue(String.class).equals(e1.getText().toString()))
                        bloodGroup = dataSnapshot.child("bloodGroup").getValue(String.class);
                    }
                    Intent intent = new Intent(SignInActivity.this, NeedyActivity.class);
                    intent.putExtra("donorBloodGroup",bloodGroup);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(SignInActivity.this, "Invalid Phone no !!", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SignInActivity.this, "Donor Database not found !!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


