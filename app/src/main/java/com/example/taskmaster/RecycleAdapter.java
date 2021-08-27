package com.example.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
  private OnClickListener listener;
  private List<Task> tasksList = new ArrayList<>();

  public RecycleAdapter() {
  }

  public RecycleAdapter(OnClickListener listener, List<Task> tasksList) {
    this.listener = listener;
    this.tasksList = tasksList;
  }

  public interface OnClickListener {
    void onTaskClicked(int position);

    void onTaskDelete(int position);
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
    return new MyViewHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    String title = tasksList.get(position).getTitle();
    holder.title.setText(title);
    String state = tasksList.get(position).getState();
    holder.state.setText(state);

    String body = tasksList.get(position).getBody();
    holder.body.setText(body);
  }

  @Override
  public int getItemCount() {
    return tasksList.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView body;
    private final TextView state;



    public MyViewHolder(@NonNull View view, OnClickListener listener) {
      super(view);
      title = view.findViewById(R.id.title1);
      body = view.findViewById(R.id.body);
      state = view.findViewById(R.id.taskState);
      ImageView imageView = view.findViewById(R.id.img);
      Button deleteBtn = view.findViewById(R.id.delete);

      imageView.setOnClickListener(v -> listener.onTaskClicked(getBindingAdapterPosition()));
      deleteBtn.setOnClickListener(v -> listener.onTaskDelete(getBindingAdapterPosition()));
    }
  }
}
