package com.hina.trial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    Button signin;
    EditText email,pass;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(MainActivity.mSharedPrefName, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.txt_sign_up);
        signin=findViewById(R.id.btn_sign_in);
        email = findViewById(R.id.edt_email);
        pass = findViewById(R.id.edt_pass);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //same as !TextUtils.isEmpty(email.getText()) email.getText().length()!=0
                if(!TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(pass.getText())){

                    mAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("user", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        final SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putStringSet("user",user.);
                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                                        editor.putBoolean("login",true);
                                        rootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               for(DataSnapshot child : dataSnapshot.getChildren()){
                                                    Log.e(child.getKey(),child.getValue(String.class));
                                                    editor.putString(child.getKey(),child.getValue(String.class));
                                                }
                                                editor.commit();
                                                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                                                startActivity(i);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });



                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("user", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
