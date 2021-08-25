package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class Login extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
      EditText name = findViewById(R.id.user);
      EditText password = findViewById(R.id.password11);



      Button home = findViewById(R.id.home1);
      home.setOnClickListener(v -> {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);


      });
      Intent in= new Intent(getApplicationContext(),MainActivity.class);
    Button login = findViewById(R.id.enter11);

    login.setOnClickListener(v -> {

      String username= name.getText().toString();
      String pass= password.getText().toString();
      Amplify.Auth.signIn(
        username,
        pass,
        result -> {if(result.isSignInComplete())  {startActivity(in);}} ,
        error -> Log.e("AuthQuickstart", error.toString())
      );

    });




    }




}
