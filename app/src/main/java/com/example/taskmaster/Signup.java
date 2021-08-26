package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

      EditText name = findViewById(R.id.editName);
      EditText email = findViewById(R.id.editEmail);
      EditText pass = findViewById(R.id.editPass);



      SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
      SharedPreferences.Editor editor= preferences.edit();


      /////////// ----add auth configurations ----////////////
//      setAuthConfiguration();
      try {
        Amplify.addPlugin(new AWSDataStorePlugin());
        Amplify.addPlugin(new AWSApiPlugin());
        Amplify.addPlugin(new AWSCognitoAuthPlugin());
        Amplify.addPlugin(new AWSS3StoragePlugin());
        Amplify.configure(getApplicationContext());


        Log.i("Tutorial", "Initialized Amplify");
      } catch (AmplifyException e) {
        Log.e("Tutorial", "Could not initialize Amplify", e);
      }


      Button register = findViewById(R.id.signup);
      register.setOnClickListener(v -> {
        String username= name.getText().toString();
        String emailUser= email.getText().toString();
        String password= pass.getText().toString();
        editor.putString("name", username);
        editor.apply();
        // add registration things :
        registration( username ,  emailUser , password);
      });
      Button btn= findViewById(R.id.home2);
      btn.setOnClickListener(v -> {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
      });


      Button verifyBtn= findViewById(R.id.verfiy);
      verifyBtn.setOnClickListener(v -> {
        Intent intent=new Intent(getApplicationContext(),Confirmation.class);
        startActivity(intent);
      });

      Button signInToSign= findViewById(R.id.goSign);
      signInToSign.setOnClickListener(v -> {
        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
      });
    }


  /**
   * to set auth configuration
   */
//  public void setAuthConfiguration(){
//    // add amplify things :
//
//    try {
//
//      Amplify.addPlugin(new AWSDataStorePlugin());
//      Amplify.addPlugin(new AWSApiPlugin());
//      Amplify.configure(getApplicationContext());
//      Amplify.addPlugin(new AWSCognitoAuthPlugin());
//      Amplify.configure(getApplicationContext());
//
//      Log.i("Tutorial", "Initialized Amplify");
//    } catch (AmplifyException e) {
//      Log.e("Tutorial", "Could not initialize Amplify", e);
//      e.printStackTrace();
//      e.printStackTrace();
//    }
//
//  }




  public void registration(String username , String email , String pass){
    AuthSignUpOptions options = AuthSignUpOptions.builder()
      .userAttribute(AuthUserAttributeKey.email(), email)
      .build();
    Amplify.Auth.signUp(username, pass, options,
      result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
      error -> Log.e("AuthQuickStart", "Sign up failed", error)
    );

  }



}
