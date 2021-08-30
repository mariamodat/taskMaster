package com.example.taskmaster;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity2 extends AppCompatActivity  {
// initializing
  // FusedLocationProviderClient
  // object
  FusedLocationProviderClient mFusedLocationClient;
  TextView latitudeTextView, longitTextView;
  int PERMISSION_ID = 44;

  private static final String TAG = "tag";
 private static final ArrayList<Team> teams = new ArrayList<>();
  private static final int REQUEST_FOR_FILE = 1;
  Spinner spinner;
  TextView textFile;
  String fileType;
  String fileName;
  File uploadFile;
  EditText taskDesc;
  String imgSrc;

  private static final int PICKFILE_RESULT_CODE = 1;
  Handler handler= new Handler(Looper.getMainLooper(), msg -> {

    Log.i(TAG, "onCreate: " + teams.isEmpty());
    return true;
  });


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);

    latitudeTextView = findViewById(R.id.latTextView);
    longitTextView = findViewById(R.id.lonTextView);

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    // method to get the location
    getLastLocation();

//    Team team = Team.builder().name("test").build();
//    saveTeamsToApi(team);

    // get the teams from api and save it in the teams list
    getTeamsFromApi();


    EditText title = findViewById(R.id.titleA);
    EditText status = findViewById(R.id.status);
    EditText desc = findViewById(R.id.desc);


// Get intent, action and MIME type

    Intent receiveFromApp = getIntent();
    Bundle extars= receiveFromApp.getExtras();
      String action = receiveFromApp.getAction();
    String type = receiveFromApp.getType();
    if (Intent.ACTION_SEND.equals(action) && type != null) {
      if ("text/plain".equals(type)) {
        System.out.println("extraaas>>>>" +extars );
        handleSendText(receiveFromApp); // Handle text being sent
      } else if (type.startsWith("image/")) {
        handleSendImage(receiveFromApp); // Handle single image being sent
      }
    }


//    Task task = Task.builder().title("meme").body("cute").state("new").team(team).build();
//    saveTasksToApi(task);
    /////////////--making spinner things:--////////////
    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
      android.R.layout.simple_spinner_item,
      new String[]{"Team 1", "Team 2", "Team 3"});
    spinner = findViewById(R.id.spinner);
    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter2);

    /**
     * upload file  button listener
     */
    Button buttonPick = (Button)findViewById(R.id.pick);
    textFile = (TextView)findViewById(R.id.textfile);
    buttonPick.setOnClickListener(arg0 -> {
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("*/*");
      startActivityForResult(intent,PICKFILE_RESULT_CODE);

    });



    // creating the
//    Team team = Team.builder().name("Team 1").build();
//    Team team2 = Team.builder().name("Team 2").build();
//    Team team3 = Team.builder().name("Team 3").build();
//    saveTeamsToApi(team);
//    saveTeamsToApi(team2);
//    saveTeamsToApi(team3);


    Button submitB = findViewById(R.id.addTask);
    submitB.setOnClickListener(v -> {

      String titleName = title.getText().toString();
      String titleDesc = desc.getText().toString();
      String titleStatus = status.getText().toString();
      String teamSelected = spinner.getSelectedItem().toString();
      Log.i(TAG, "onClick: " + teamSelected);
      switch (teamSelected) {
        case ("Team 1"):
          Task tas1 = Task.builder().title(titleName).body(titleDesc).state(titleStatus).team(teams.get(0)).fileName(fileName).build();
          saveTasksToApi(tas1);

          break;
        case ("Team 2"):
          Task tas2 = Task.builder().title(titleName).body(titleDesc).state(titleStatus).team(teams.get(1)).fileName(fileName).build();
          saveTasksToApi(tas2);
          break;
        case ("Team 3"):
          Task tas3 = Task.builder().title(titleName).body(titleDesc).state(titleStatus).team(teams.get(2)).fileName(fileName).build();
          saveTasksToApi(tas3);
          break;
      }

    });

  }



  /**
   * handle the received files
   * @param intent
   */
  void handleSendText(Intent intent) {
    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
    if (sharedText != null) {
      // Update UI to reflect text being shared

    }
  }

  /**
   * handle shared images
   * @param intent
   */
  void handleSendImage(Intent intent) {
    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
    if (imageUri != null) {
      // Update UI to reflect image being shared
    }
  }




  /**
   * get teams from api and save it in teams list
   */
  public void getTeamsFromApi() {
    Amplify.API.query(ModelQuery.list(Team.class),
      response -> {
        for (Team t : response.getData()) {
          teams.add(t);
          Log.i(TAG, "teams from Api  " + t.getName());
          System.out.println("teams from api>>>>>>>>>>>>>>>   " + t.getName());
          handler.sendEmptyMessage(1);
        }

      },
      error -> Log.e(TAG, "Failed to get Teams => " + error.toString())
    );
  }

  public void saveTasksToApi(Task task) {
    Amplify.API.mutate(ModelMutation.create(task),
      success -> Log.i(TAG, "Saved item to API " + success.getData()),
      error -> Log.e(TAG, "Could not save item to API/dynamodb", error));

  }

  @RequiresApi(api = Build.VERSION_CODES.Q)
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    Uri uri=data.getData();
    fileType =getContentResolver().getType(uri);
    if (requestCode == REQUEST_FOR_FILE && resultCode == RESULT_OK) {
      Log.i(TAG, "onActivityResult: File Type -> "+fileType);
      Log.i(TAG, "onActivityResult: returned from file explorer");
      Log.i(TAG, "onActivityResult: => " + data.getData());
       uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFile");
      fileName=new SimpleDateFormat("yyMMddHHmmssZ").format(new Date())+"."+fileType.split("/")[1];
      try {
        InputStream inputStream = getContentResolver().openInputStream(data.getData());
        FileUtils.copy(inputStream, new FileOutputStream(uploadFile));
      } catch (Exception exception) {
        Log.e(TAG, "onActivityResult: file upload failed" + exception.toString());
      }


      Amplify.Storage.uploadFile(
        fileName,
        uploadFile,
        success -> {
          Log.i(TAG, "uploadFileToS3: succeeded " + success.getKey());
        },
        error -> {
          Log.e(TAG, "uploadFileToS3: failed " + error.toString());
        }
      );
      String FilePath = data.getData().getPath();
      textFile.setText(FilePath);

    } }

    public void saveTeamsToApi (Team team){
      Amplify.API.mutate(ModelMutation.create(team), success -> Log.i(TAG, "Saved team to API " + success.getData().getName()),
        error -> Log.e(TAG, "Could not save team to API/dynamodb", error));
    }

    @Override
    protected void onResume () {
      super.onResume();
      if (checkPermissions()) {
        getLastLocation();
      }

    }


  @SuppressLint("MissingPermission")
  private void getLastLocation() {
    // check if permissions are given
    if (checkPermissions()) {

      // check if location is enabled
      if (isLocationEnabled()) {

        // getting last
        // location from
        // FusedLocationClient
        // object
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
          @Override
          public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
            Location location = task.getResult();
            if (location == null) {
              requestNewLocationData();
            } else {
              latitudeTextView.setText(location.getLatitude() + "");
              longitTextView.setText(location.getLongitude() + "");
            }
          }
        });
      } else {
        Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
      }
    } else {
      // if permissions aren't available,
      // request for permissions
      requestPermissions();
    }
  }

  // method to request for permissions
  private void requestPermissions() {
    ActivityCompat.requestPermissions(this, new String[]{
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
  }
  // method to check
  // if location is enabled
  private boolean isLocationEnabled() {
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  }

  // method to check for permissions
  private boolean checkPermissions() {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    // If we want background location
    // on Android 10.0 and higher,
    // use:
    // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
  }

  @SuppressLint("MissingPermission")
  private void requestNewLocationData() {

    // Initializing LocationRequest
    // object with appropriate methods
    LocationRequest mLocationRequest = new LocationRequest();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setInterval(5);
    mLocationRequest.setFastestInterval(0);
    mLocationRequest.setNumUpdates(1);

    // setting LocationRequest
    // on FusedLocationClient
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
  }

  private LocationCallback mLocationCallback = new LocationCallback() {

    @Override
    public void onLocationResult(LocationResult locationResult) {
      Location mLastLocation = locationResult.getLastLocation();
      latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
      longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
    }
  };
  // If everything is alright then
  @Override
  public void
  onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == PERMISSION_ID) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        getLastLocation();
      }
    }
  }

//  // Get a handle to the GoogleMap object and display marker.
//  @Override
//  public void onMapReady(@NonNull GoogleMap googleMap) {
//    googleMap.addMarker(new MarkerOptions()
//      .position(new LatLng(0, 0))
//      .title("Marker"));
//  }

}
