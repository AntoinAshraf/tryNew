package com.example.antoin_ashraf.trynew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class AddTask extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference DBRef;
    EditText editTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        database = FirebaseDatabase.getInstance();
    }

    public void OnSaveClick(View view) {
        editTask = (EditText)findViewById(R.id.message);
        long date = System.currentTimeMillis();

        String name = editTask.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyy h:mm a");
        String dateString = sdf.format(date);

        DBRef = database.getInstance().getReference().child("Tasks");

        DatabaseReference newTask = DBRef.push();
        newTask.child("name").setValue(name);
        newTask.child("time").setValue(dateString);

        Toast.makeText(this, "Task " + name + " is  Added", Toast.LENGTH_SHORT).show();

        Intent MainActivityIntent = new Intent(AddTask.this, MainActivity.class);
        startActivity(MainActivityIntent);

    }
}
