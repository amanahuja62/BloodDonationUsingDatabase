package com.example.blooddonation;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class AcceptorRegisterActivity extends AppCompatActivity {
   EditText e1, e2, e3;
   Button btn;

   final static int READ_EXTERNAL_STORAGE_CODE =101;
   final static int CAMERA_REQUEST_CODE = 102;
   final static int PICK_IMAGE_GALLERY = 120;
   final static int PICK_IMAGE_CAMERA =121;

   String name,address,mobile;
   String bloodGroup = "O+";  //default selected blood group
   ImageView imageView;
   String[] bloods = {"O+", "A+","B+","B-","A-","O-","AB"};
   String[] optionsForImages = {"Choose from Gallery", "Click from camera"};
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;
   Acceptor acceptor;  // acceptor object contains details like name, id, address, mobile

    //method which checks and asks for permissions
    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptor_register);
        //initialisations......
        e1 = findViewById(R.id.ed1);
        e2 = findViewById(R.id.ed2);
        e3 = findViewById(R.id.ed3);
        btn = findViewById(R.id.bloods);
        imageView = findViewById(R.id.imageView2);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("AcceptorInfo"); // will be used for storing acceptor details

        //listener on save button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBloodChoiceDialog((Button) v);
            }
        });

        //listener on imageview
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagesChoiceDialog(v);

            }
        });
    }


    // this method shows various options for uploading image like gallery, camera in a dialog
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
    // this method is executed when user returns after clicking or selecting image from gallery
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



    //this method shows a dialog for selecting the blood group
    private void showBloodChoiceDialog(Button v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(bloods, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                v.setTextColor(Color.BLACK);
                v.setText(bloods[which]);
                bloodGroup = bloods[which];

            }
        });
        builder.show();
    }




    // this method is triggered when admin clicks on the save button. It stores all user data to realtime database
    // acceptors's photo is saved in cloud and its url is saved in realtime database
    public void storeDetails(View view) {

        name = e1.getText().toString();
        address = e2.getText().toString();
        mobile = e3.getText().toString();

       // for storing acceptor's photo to cloud
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://blooddonation-bd419.appspot.com/images");
        StorageReference ref = storageRef.child("images/" + UUID.randomUUID().toString());
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

       ref.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onComplete( Task<UploadTask.TaskSnapshot> task) {
                 ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         progressDialog.dismiss();
                         String url = uri.toString(); // url of image saved in cloud
                         Toast.makeText(AcceptorRegisterActivity.this, url, Toast.LENGTH_SHORT).show();
                         Log.d("urlurl",url);
                         addDatatoFirebase(name, address, mobile,bloodGroup,url); // all user data (including imgae url) added to realtime database
                     }

                     private void addDatatoFirebase(String name, String address, String mobile, String bloodGroup, String url) {
                         String acceptorID = databaseReference.push().getKey(); //every acceptor will have a unique id
                         acceptor = new Acceptor(url,name,address,mobile,bloodGroup, acceptorID);

                         databaseReference.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 databaseReference.child(acceptorID).setValue(acceptor);  // this adds acceptor data to realtime database
                                 Toast.makeText(AcceptorRegisterActivity.this, "data added", Toast.LENGTH_SHORT).show();
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {
                                 Toast.makeText(AcceptorRegisterActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                             }
                         });

                     }
                 });
           }
       }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Error, Image not uploaded
                progressDialog.dismiss();
                Toast.makeText(AcceptorRegisterActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}