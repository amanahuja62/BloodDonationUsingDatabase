package com.example.blooddonation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class AcceptorRegisterActivity extends AppCompatActivity {
   EditText e1, e2, e3;
   Button btn;
   final static int READ_EXTERNAL_STORAGE_CODE =101;
   final static int CAMERA_REQUEST_CODE = 102;
   final static int PICK_IMAGE_GALLERY = 120;
   final static int PICK_IMAGE_CAMERA =121;

   String name,address,mobile,bloodGroup;
   ImageView imageView;
    String[] bloods = {"O+", "A+","B+","B-","A-","O-","AB"};
    String[] optionsForImages = {"Choose from Gallery", "Click from camera"};
    ArrayAdapter arrayAdapter;
    AcceptorDatabase acceptorDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptor_register);
        e1 = findViewById(R.id.ed1);
        e2 = findViewById(R.id.ed2);
        e3 = findViewById(R.id.ed3);
        btn = findViewById(R.id.bloods);
        imageView = findViewById(R.id.imageView2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBloodChoiceDialog((Button) v);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagesChoiceDialog(v);

            }
        });

        acceptorDatabase = new AcceptorDatabase(this);


    }

    private void showImagesChoiceDialog(View v) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(true);
        dialog.setSingleChoiceItems(optionsForImages, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0: checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE );
                        if (ContextCompat.checkSelfPermission(AcceptorRegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_DENIED)
                        {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                        }
                            break;
                    case 1:
                        checkPermission(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE );
                        if (ContextCompat.checkSelfPermission(AcceptorRegisterActivity.this,Manifest.permission.CAMERA ) != PackageManager.PERMISSION_DENIED)
                        {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
                        }
                        break;
                }
            }
        });
        dialog.show();
    }

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }

    }

    private void showBloodChoiceDialog(Button v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(bloods, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                v.setTextColor(Color.BLACK);
                v.setText(bloods[which]);

            }
        });
        builder.show();
    }


    public void storeDetails(View view) {
        name = e1.getText().toString();
        address = e2.getText().toString();
        mobile = e3.getText().toString();
        acceptorDatabase.saveData(name,address,bloodGroup,mobile);
        Intent intent = new Intent(AcceptorRegisterActivity.this, AcceptorRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }

        if(requestCode == PICK_IMAGE_CAMERA && resultCode == Activity.RESULT_OK){
            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            // Set the image in imageview for display
            imageView.setImageBitmap(photo);
        }
    }


}