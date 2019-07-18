package com.hina.trial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    EditText email,pass,name,mob;
    Button signup,image;
    ImageView imageView;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    boolean filechoosed = false;
    String downloadUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference("users");

        signup = findViewById(R.id.btn_sign_up);
        image = findViewById(R.id.btn_image);
        email = findViewById(R.id.edt_email);
        pass = findViewById(R.id.edt_pass);
        name= findViewById(R.id.edt_name);
        mob = findViewById(R.id.edt_mob);
        imageView = findViewById(R.id.imageView);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().length() != 0 && pass.getText().length() != 0 && name.getText().length() != 0 && mob.getText().length() != 0 && !filechoosed){
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("user", "createUserWithEmail:success");

                                        String id = rootRef.push().getKey();

                                        User newUser = new User(id,name.getText().toString(),mob.getText().toString(),email.getText().toString(),downloadUrl );
                                        rootRef.child(mAuth.getUid()).setValue(newUser);

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        final SharedPreferences.Editor editor = getSharedPreferences(MainActivity.mSharedPrefName,MODE_PRIVATE).edit();
                                        editor.putBoolean("login",true);
                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                                        rootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                                    Log.e(child.getKey(),child.getValue(String.class));
                                                    editor.putString(child.getKey(),child.getValue(String.class));
                                                }
                                                editor.commit();
                                                Intent i = new Intent(RegisterActivity.this,HomeActivity.class);
                                                startActivity(i);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });



                                    }else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("user", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.\n\n" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!filechoosed)
                    showFileChooser();
                else
                {
                    //uploadImage();
                }
            }
        });


    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
                filechoosed=true;
                imageView.setVisibility(View.VISIBLE);
                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){

        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading");
//            progressDialog.show();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");

            StorageReference storageReference= FirebaseStorage.getInstance().getReference();
            final StorageReference riversRef = storageReference.child("images/"+ft.format(dNow)+".jpg");
            Task<Uri> uriTask = riversRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        filePath = task.getResult();
                        Log.e("File Uploaded", filePath.toString());
                        //downloadUrl="https://firebasestorage.googleapis.com"+filePath.getPath();
                        downloadUrl=filePath.toString();
                        Picasso.get().load(downloadUrl).fit().into(imageView);
                    }
                    else{
                        Log.e("File Upload Error",task.getException().getMessage(),task.getException());
                        Toast.makeText(RegisterActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                    filechoosed = false;
                }
            });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }

    }

}
