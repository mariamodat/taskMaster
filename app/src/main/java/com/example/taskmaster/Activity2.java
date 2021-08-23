package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {

  private static final String TAG = "tag";
  private List<Team> teams = new ArrayList<>();
  Spinner spinner;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);


    EditText title = (EditText) findViewById(R.id.titleA);
    EditText status = findViewById(R.id.status);
    EditText desc = (EditText) findViewById(R.id.desc);
    String titleName = title.getText().toString();
    String titleDesc = desc.getText().toString();
    String titleStatus = status.getText().toString();
    getTeamsFromApi();
    /////////////--making spinner things:--////////////
    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
      android.R.layout.simple_spinner_item,
      new String[]{"Team 1", "Team 2", "Team 3"});
    spinner = findViewById(R.id.spinner);
    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(dataAdapter2);

//    Team team = Team.builder().name("Team 1").build();
//    Team team2 = Team.builder().name("Team 2").build();
//    Team team3 = Team.builder().name("Team 3").build();
//    saveTeamsToApi(team);
//    saveTeamsToApi(team2);
//    saveTeamsToApi(team3);


    /**
     * save button listener
     */
    Button submitB = (Button) findViewById(R.id.addTask);
    submitB.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        String teamSelected = spinner.getSelectedItem().toString();

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
      }
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
