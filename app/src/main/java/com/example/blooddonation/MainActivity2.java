package com.example.blooddonation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText e1, e2, e3, e4, e5;
    Spinner spinner;
    String name, address, dob, dateDonation, mobileNo, bloodGroup;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CheckBox checkBox;
    String[] s = {"O+", "A+", "B+", "B-", "A-", "O-", "AB"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialise();
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                e3.setText(Integer.toString(dayOfMonth) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(year));

            }
        };
        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                e4.setText(Integer.toString(dayOfMonth) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(year));

            }
        };
        //listener for date of birth field
        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity2.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity2.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //listener for spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodGroup = s[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void initialise() {
        e1 = findViewById(R.id.e21);  // name
        e2 = findViewById(R.id.e22);  // address
        e3 = findViewById(R.id.e23); // date of birth
        e4 = findViewById(R.id.e24); // last date of blood donation
        e5 = findViewById(R.id.e25); // mobile no
        checkBox = findViewById(R.id.checkBox); // checkbox
        spinner = findViewById(R.id.spinner);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("DonorInfo"); // will be used for storing donor details

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, s);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public void saveDetails(View view) {

        if (checkBox.isChecked()) {
            name = e1.getText().toString();
            address = e2.getText().toString();
            dob = e3.getText().toString();
            dateDonation = e4.getText().toString();
            mobileNo = e5.getText().toString();
            //checking if already signed up with same phone no
            Query query = databaseReference.orderByChild("mobileNo").equalTo(mobileNo);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        e5.setError("Already registered with this phone number !!");
                    }
                    else{
                        addDatatoFirebase(name,address,mobileNo,bloodGroup,dob,dateDonation);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


        } else {
            Toast.makeText(this, "Kindly accept the terms and conditions", Toast.LENGTH_SHORT).show();
        }
    }





    private void addDatatoFirebase(String name, String address, String mobile, String bloodGroup, String dob, String dateDonation) {
        String donorID = databaseReference.push().getKey(); //every donor will have a unique id
        Donor donor = new Donor(name, address, dob, bloodGroup, dateDonation, mobile, donorID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(donorID).setValue(donor);  // this adds donor's data to realtime database
                Toast.makeText(MainActivity2.this, "data added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity2.this, NeedyActivity.class);
                intent.putExtra("donorBloodGroup", bloodGroup);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
