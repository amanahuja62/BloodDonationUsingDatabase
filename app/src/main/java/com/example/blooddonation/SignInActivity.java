package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    DonorsDatabase donorsDatabase;
    EditText e1;
    String bloodGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        donorsDatabase = new DonorsDatabase(this);
        e1 = findViewById(R.id.editTextTextPersonName2);
    }

    public void signinmethod(View view) {
        if(donorsDatabase.isPresent(e1.getText().toString())){
            bloodGroup = donorsDatabase.getBloodGroup(e1.getText().toString());
            Intent intent = new Intent(SignInActivity.this, NeedyActivity.class);
            intent.putExtra("donorBloodGroup",bloodGroup);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Invalid Phone no !!", Toast.LENGTH_SHORT).show();
        }
    }
}