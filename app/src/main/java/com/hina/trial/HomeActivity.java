package com.hina.trial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    TextView name,mob,email;
    Button logout;
    ImageView profile;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(MainActivity.mSharedPrefName, MODE_PRIVATE);

        name = findViewById(R.id.txt_username);
        mob = findViewById(R.id.txt_mobileno);
        email = findViewById(R.id.txt_email);
        logout = findViewById(R.id.btn_logout);
        profile = findViewById(R.id.profile_image);

        String user_name = sharedPreferences.getString("name","demo name");
        String user_mob = sharedPreferences.getString("mob","demo mob");
        String user_email = sharedPreferences.getString("email","demo email");
        String image_path = sharedPreferences.getString("profile","");

        Log.e("user",user_name + "  "+user_mob+"   "+user_email);
        name.setText(user_name);
        mob.setText(user_mob);
        email.setText(user_email);
        if(image_path!= ""){
            Picasso.get().load(image_path)
                    .into(profile);

        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });
    }

}
