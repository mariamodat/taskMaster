package com.example.taskmaster;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {

  private static final String TAG = "tag";
 private static final ArrayList<Team> teams = new ArrayList<>();
  Spinner spinner;

  Handler handler= new Handler(Looper.getMainLooper(), msg -> {

    Log.i(TAG, "onCreate: " + teams.isEmpty());
    return true;
  });


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);

//
//    Team team = Team.builder().name("test").build();
//    saveTeamsToApi(team);

    // get the teams from api and save it in the teams list
    getTeamsFromApi();


    EditText title = findViewById(R.id.titleA);
    EditText status = findViewById(R.id.status);
    EditText desc = findViewById(R.id.desc);





//    Task task = Task.builder().title("meme").body("cute").state("new").team(team).build();
//    saveTasksToApi(task);
    /////////////--making spinner things:--////////////
    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
      android.R.layout.simple_spinner_item,
      new String[]{"Team 1", "Team 2", "Team 3"});
    spinner = findViewById(R.id.spinner);
    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter2);


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
          Task tas1 = Task.builder().title(titleName).body(titleDesc).state(titleStatus).team(teams.get(0)).build();
          saveTasksToApi(tas1);

          break;
        case ("Team 2"):
          Task tas2 = Task.builder().title(titleName).body(titleDesc).state(titleStatus).team(teams.get(1)).build();
          saveTasksToApi(tas2);
          break;
        case ("Team 3"):
          Task tas3 = Task.builder().title(titleName).body(titleDesc).state(titleStatus).team(teams.get(2)).build();
          saveTasksToApi(tas3);
          break;
      }

//        Intent intent = new Intent(Activity2.this, MainActivity.class);
//        startActivity(intent);
    });

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




  public void saveTeamsToApi(Team team){
    Amplify.API.mutate(ModelMutation.create(team), success -> Log.i(TAG, "Saved team to API " + success.getData().getName()),
      error -> Log.e(TAG, "Could not save team to API/dynamodb", error));
  }

  @Override
  protected void onResume() {
    super.onResume();

  }
}
