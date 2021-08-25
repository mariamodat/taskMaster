package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
      EditText ot = findViewById(R.id.otp);

      SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


      // add confirmation :


      Button signin = findViewById(R.id.confirm);
      signin.setOnClickListener(v -> {
        String otp = ot.getText().toString();
        String username =sharedPreferences.getString("name","hi");
        confirmation( otp , username);
        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
      });
    }


  public void confirmation(String otp ,String username){
    Amplify.Auth.confirmSignUp(
      username,
      otp,
      result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
      error -> Log.e("AuthQuickstart", error.toString())
    );
  }

}
