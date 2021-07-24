package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class AcceptorDetailsActivity extends AppCompatActivity {
   TextView t1,t2,t3,t4;
   String bloodGroup;
   String rowNo;
   int row;
   AcceptorDatabase acceptorDatabase;
   ArrayList arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptor_details);
        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView4);
        t3 = findViewById(R.id.textView5);
        t4 = findViewById(R.id.textView6);
        bloodGroup = getIntent().getStringExtra("bloodGroup");
        rowNo = getIntent().getStringExtra("rowno");
        row = Integer.valueOf(rowNo);

        acceptorDatabase = new AcceptorDatabase(this);
        arrayList=acceptorDatabase.getAllDetails(bloodGroup,row);


        t1.setText(arrayList.get(0).toString());
        t2.setText(arrayList.get(1).toString());
        t3.setText(arrayList.get(2).toString());
        t4.setText(arrayList.get(3).toString());



    }
}