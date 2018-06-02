package com.example.santosh.sochware_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,password,name;
    private Button signin,signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        signin=(Button)findViewById(R.id.signin);
        signup=(Button)findViewById(R.id.signup);
        email=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
        name=(EditText)findViewById(R.id.etName);

        //check if user is already loggedIn
        if (mAuth.getCurrentUser()!=null)
        {
            //user not logged in
            finish();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail=email.getText().toString().trim();
                String getepassword=password.getText().toString().trim();
                callsignin(getemail,getepassword);

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getemail=email.getText().toString().trim();
                String getepassword=password.getText().toString().trim();
                callsignup(getemail,getepassword);
            }
        });


    }
private  void callsignup(String email,String password) {
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Testing", "Sign up Successful");
                    if (!task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "Sign up Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        userProfile();
                        Toast.makeText(MainActivity.this, "created account", Toast.LENGTH_SHORT).show();
                        Log.d("TESTING", "Created Account");


                    }
                }



            });
}
//set user display name
private void userProfile()
{
    FirebaseUser user = mAuth.getCurrentUser();
    if(user!=null)
    {
        UserProfileChangeRequest profileUpdates= new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString().trim())
                //
        .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("TESTING","user Profile updated.");
                        }
                    }
                });

    }
}

//sign in process
private void callsignin(String email,String password){

    mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  Log.d("TESTING","sign in sucessful:" +task.isSuccessful());


                  if (!task.isSuccessful()){
                      Log.v("Testing","SignInWithEmail:Failed",task.getException());
                      Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                  }
                  else {
                      Intent i = new Intent(MainActivity.this,SignIn.class);
                      finish();
                      startActivity(i);
                  }

                }
            });
}
}
