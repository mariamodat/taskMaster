package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
  public static final String TASK_NAME = "taskTitle";
  private static final String TAG = "tag";
  RecycleAdapter recycleAdapter = new RecycleAdapter();
  private final ArrayList<Task> taskList = new ArrayList<>();
  private  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      recyclerView=  findViewById(R.id.recyclerView1);
      // set Amplify things here :
      // add amplify things :
      try {

        Amplify.addPlugin(new AWSDataStorePlugin());
        Amplify.addPlugin(new AWSApiPlugin());
        Amplify.configure(getApplicationContext());

        Log.i("Tutorial", "Initialized Amplify");
      } catch (AmplifyException e) {
        Log.e("Tutorial", "Could not initialize Amplify", e);
      }


      Task t1 = Task.builder().title("Mariam").body("hello").build();
      taskList.add(t1);

      Button addBtn= findViewById(R.id.addBtn);
      addBtn.setOnClickListener(v -> {
        Intent intent = new Intent(MainActivity.this, Activity2.class);
        startActivity(intent);
        Toast toast = Toast.makeText(getApplicationContext(), "add new", Toast.LENGTH_SHORT);
        toast.show();
      });
    }

  /**
   * set the adapter view so we can retrieve data from tasklist
   */
  private void setAdapter(){
    RecycleAdapter adapter = new RecycleAdapter(new RecycleAdapter.OnClickListener(){

      @Override
      public void onTaskClicked(int position) {
        Intent intent = new Intent(getApplicationContext(), TaskDetails.class);
//        TextView TextTaskTitle = findViewById(R.id.taskText);
        intent.putExtra(TASK_NAME, taskList.get(position).getTitle());
        startActivity(intent);
      }

      @SuppressLint("NotifyDataSetChanged")
      @Override
      public void onTaskDelete(int position) {
        //        dao.remove(taskList.get(position));
        taskList.remove(position);
        for (Task tt : taskList) {
          System.out.println("task title is :   " + tt.getTitle());
        }

        recycleAdapter.notifyDataSetChanged();
      }
    }, taskList);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(adapter);
  }


  /**
   * to get the data from api then save in the taskList
   */
  private void getTaskFromAPI(){
    Amplify.API.query(ModelQuery.list(Task.class), response ->
      {
        for (Task t : response.getData()){
          taskList.add(t);
          Log.i(TAG, "onCreate: the tasks are => " + t.getTitle());
        }
        handler.sendEmptyMessage(1);
      },
      error -> Log.e(TAG, "onCreate: Failed to get task => " + error.toString())

    );

  }

  /**
   * make the handler to handle changes
   */
  private final Handler handler = new Handler(Looper.getMainLooper(),
    new Handler.Callback() {
      @SuppressLint("NotifyDataSetChanged")
      @Override
      public boolean handleMessage(@NonNull Message msg) {
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

        return false;

      }
    });

  @Override
  protected void onResume() {
    super.onResume();
    // get data from Api
    getTaskFromAPI();
    setAdapter();

  }
}
