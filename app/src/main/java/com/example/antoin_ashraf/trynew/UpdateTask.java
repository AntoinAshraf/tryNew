package com.example.antoin_ashraf.trynew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateTask extends AppCompatActivity {

    private String task_key = null;
    private TextView  singleTime;
    String task_title, task_time;
    private EditText singleTask;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        task_key = getIntent().getExtras().getString("TaskId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tasks");

        singleTask = (EditText) findViewById(R.id.updateName_TV);
        singleTime = (TextView) findViewById(R.id.updateTime_TV);

        mDatabase.child(task_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                task_title = (String)dataSnapshot.child("name").getValue();
                task_time = (String)dataSnapshot.child("time").getValue();

                singleTask.setText(task_title);
                singleTime.setText(task_time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void OnUpdateClick(View view) {
        task_title = singleTask.getText().toString();
        task_time = singleTime.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference("Tasks").child(task_key);

        Task task = new Task(task_title, task_time);

        mDatabase.setValue(task);
        Toast.makeText(this, "task "+ task_key +" Updated", Toast.LENGTH_SHORT).show();

        Intent MainActivityIntent = new Intent(UpdateTask.this, MainActivity.class);
        startActivity(MainActivityIntent);
    }
}
