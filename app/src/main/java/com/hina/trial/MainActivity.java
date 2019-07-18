package com.hina.trial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static String mSharedPrefName = "trial_data";
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(mSharedPrefName , MODE_PRIVATE);
//        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent i= new Intent(MainActivity.this,HomeActivity.class);
//            i.putExtra("user",currentUser);
//            startActivity(i);
//        }
//        else{
//            Intent i= new Intent(MainActivity.this,LoginActivity.class);
//            startActivity(i);
//        }
        boolean login = sharedPreferences.getBoolean("login",false);
        if(login){
            Intent i= new Intent(MainActivity.this,HomeActivity.class);
           // i.putExtra("user",currentUser);
            startActivity(i);
        }
        else{
            Intent i= new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }
    }
}
