package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NeedyActivity extends AppCompatActivity {
    ListView listView;
    ArrayList arrayList;
    String donorBloodGroup;
    AcceptorDatabase acceptorDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needy);
        listView = findViewById(R.id.listview);
        acceptorDatabase = new AcceptorDatabase(this);
        donorBloodGroup=getIntent().getStringExtra("donorBloodGroup");

        Log.i("blood group",getIntent().getStringExtra("donorBloodGroup"));
        arrayList = acceptorDatabase.getNameandAddress(getIntent().getStringExtra("donorBloodGroup"));
        if(arrayList.size()>=1) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(NeedyActivity.this, AcceptorDetailsActivity.class);
                    intent.putExtra("rowno",Integer.toString(position));
                    intent.putExtra("bloodGroup",donorBloodGroup);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(this, "No acceptor with blood group "+getIntent().getStringExtra("donorBloodGroup"), Toast.LENGTH_SHORT).show();
        }

    }
}