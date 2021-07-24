package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {
   final Calendar myCalendar = Calendar.getInstance();
   EditText e1,e2,e3,e4,e5;
   Spinner spinner;
   String name,address,dob,dateDonation,mobileNo,bloodGroup;
   DonorsDatabase donorsDatabase;
   CheckBox checkBox;
   String[] s = {"O+", "A+","B+","B-","A-","O-","AB"};
   ArrayAdapter arrayAdapter;
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
                e3.setText(Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year));

            }
        };
        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                e4.setText(Integer.toString(dayOfMonth)+"/"+Integer.toString(month+1)+"/"+Integer.toString(year));

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

    public void initialise(){
        e1 = findViewById(R.id.e21);  // name
        e2 = findViewById(R.id.e22);  // address
        e3 = findViewById(R.id.e23); // date of birth
        e4 = findViewById(R.id.e24); // last date of blood donation
        e5 = findViewById(R.id.e25); // mobile no
        checkBox = findViewById(R.id.checkBox); // checkbox
        spinner = findViewById(R.id.spinner);
        donorsDatabase = new DonorsDatabase(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,s);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public void saveDetails(View view) {
        if(checkBox.isChecked()){
            name = e1.getText().toString();
            address = e2.getText().toString();
            dob = e3.getText().toString();
            dateDonation = e4.getText().toString();
            mobileNo = e5.getText().toString();
            if(!donorsDatabase.isPresent(mobileNo)) {
                donorsDatabase.saveData(name, address, dob, bloodGroup, dateDonation, mobileNo);
                Toast.makeText(this, "Your Data is saved !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity2.this, NeedyActivity.class);
                intent.putExtra("donorBloodGroup", bloodGroup);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Account with this phone no already exists !!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Kindly accept the terms and conditions", Toast.LENGTH_SHORT).show();
        }
    }
}