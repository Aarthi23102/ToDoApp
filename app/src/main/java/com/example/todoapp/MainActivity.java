package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    View view;
    EditText popup_task;
    DBHelper db;
    Button button;
    ArrayList<String> taskList;
    ArrayList<Integer> idList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DBHelper(this);
        button=findViewById(R.id.button);


        taskList=new ArrayList<>();
        idList=new ArrayList<>();

        listView=findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,taskList);
        listView.setAdapter(arrayAdapter);

        imageView=findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup,null);
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                popup_task=view.findViewById(R.id.task);
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskText= String.valueOf(popup_task.getText());
                        Boolean checkInsert=db.insertTask(taskText);
                        if(checkInsert)
                        {
                            Cursor cur=db.getTasks();
                            Boolean movedToLast = cur.moveToLast();
                            if(movedToLast)
                            {
                                int id=cur.getInt(0);
                                taskList.add(taskText);
                                idList.add(id);
                                Toast.makeText(MainActivity.this, "inserted successfully"+taskText+id, Toast.LENGTH_SHORT).show();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                            Toast.makeText(MainActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Closed", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur=db.getTasks();
                if(cur.getCount()==0)
                {
                    Toast.makeText(MainActivity.this, "no entries", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(cur.moveToNext())
                {
                    buffer.append("Id : "+cur.getInt(0)+"\n");
                    buffer.append("Task : "+cur.getString(1)+"\n");
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setMessage(buffer.toString());
                builder.setTitle("Tasks list");
                builder.show();
            }
        });
    }
}