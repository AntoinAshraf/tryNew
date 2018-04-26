package com.example.antoin_ashraf.trynew;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTaskList;
    private DatabaseReference mDatabase;

    TextView bannerDay, bannerDate;

    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTaskList = (RecyclerView)findViewById(R.id.task_list);
        mTaskList.setHasFixedSize(true);
        mTaskList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tasks");

        bannerDay = (TextView)findViewById(R.id.bannerDay);
        bannerDate = (TextView)findViewById(R.id.bannerDate);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String DayOfTheWeek = sdf.format(d);
        bannerDay.setText(DayOfTheWeek);


        long date = System.currentTimeMillis();
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM MM dd, yyy h:mm a");
        String dateString = sdf2.format(date);
        bannerDate.setText(dateString);

        context = MainActivity.this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView taskName, taskTime;
        public TaskViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            taskName = (TextView)mView.findViewById(R.id.taskName);
            taskName.setText(name);
        }

        public void setTime(String time){
            taskTime = (TextView)mView.findViewById(R.id.taskTime);
            taskTime.setText(time);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Task, TaskViewHolder> FBRA = new FirebaseRecyclerAdapter<Task, TaskViewHolder>(
                Task.class,
                R.layout.task_raw,
                TaskViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(TaskViewHolder viewHolder, Task model, int position) {

                final String task_key = getRef(position).getKey().toString();
                viewHolder.setName(model.getName());
                viewHolder.setTime(model.getTime());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleTaskActivity = new Intent(MainActivity.this, SingleTask.class);
                        singleTaskActivity.putExtra("TaskId", task_key);
                        startActivity(singleTaskActivity);
                    }
                });

                viewHolder.mView.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference newDBRef = FirebaseDatabase.getInstance().getReference("Tasks").child(task_key);
                        newDBRef.removeValue();

                        Toast.makeText(context,"deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.mView.findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent UpdateTaskActivity = new Intent(MainActivity.this, UpdateTask.class);
                        UpdateTaskActivity.putExtra("TaskId", task_key);
                        startActivity(UpdateTaskActivity);
                    }
                });
            }
        };

        mTaskList.setAdapter(FBRA);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.AddTask){
            Intent addIntent = new Intent(MainActivity.this, AddTask.class);
            startActivity(addIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
