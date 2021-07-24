package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    EditText e1,e2;
    // e1 is for phone no and e2 is for password
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        e1 = findViewById(R.id.editTextTextPersonName);
        e2 = findViewById(R.id.editTextTextPassword);

    }

    public void mymthd(View view) {
        if(e1.getText().toString().equals("8802235383") && e2.getText().toString().equals("amanahuja123")){
            Intent intent = new Intent(AdminActivity.this, AcceptorRegisterActivity.class);
            Toast.makeText(this, "Login Successfull !!", Toast.LENGTH_SHORT).show();
            startActivity(intent);

        }
        else{
            Toast.makeText(this, "Invalid Login Credentials !!", Toast.LENGTH_SHORT).show();
        }
    }
}