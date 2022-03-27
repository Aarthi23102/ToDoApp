package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    View view,view2;
    EditText popup_task,popup_task2;
    DBHelper db;
//    Button button;
    ArrayList<String> taskList;
    ArrayList<Integer> idList;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DBHelper(this);
//        button=findViewById(R.id.button);


        taskList=new ArrayList<>();
        idList=new ArrayList<>();

        listView=findViewById(R.id.listView);
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,taskList);
        listView.setAdapter(arrayAdapter);

        Cursor cur=db.getTasks();

        while(cur.moveToNext())
        {
            taskList.add(cur.getString(1));
            idList.add(cur.getInt(0));
            arrayAdapter.notifyDataSetChanged();
        }




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
                                Toast.makeText(MainActivity.this, "Task added successfully ", Toast.LENGTH_SHORT).show();
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




        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Is this task completed?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(MainActivity.this, taskList.get(position)+idList.get(position), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                        db.deleteTask(idList.get(position));
                        taskList.remove(position);
                        idList.remove(position);

                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                return true;
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view2= LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup,null);
                AlertDialog.Builder builder2=new AlertDialog.Builder(MainActivity.this);
                builder2.setView(view2);
                popup_task2=view2.findViewById(R.id.task);
                builder2.setCancelable(true);
                
                popup_task2.setText(taskList.get(position));



                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String new_text= String.valueOf(popup_task2.getText());
                        Boolean check_update = db.updateTask(idList.get(position),new_text);
                        if(check_update==true)
                        {
                            Toast.makeText(MainActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            taskList.set(position,new_text);
                            arrayAdapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                    }
                });

                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MainActivity.this, "Closed", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                builder2.show();
            }
        });



//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cursor cur=db.getTasks();
//                if(cur.getCount()==0)
//                {
//                    Toast.makeText(MainActivity.this, "no entries", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                StringBuffer buffer=new StringBuffer();
//                while(cur.moveToNext())
//                {
//                    buffer.append("Id : "+cur.getInt(0)+"\n");
//                    buffer.append("Task : "+cur.getString(1)+"\n");
//                }
//                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
//                builder.setCancelable(true);
//                builder.setMessage(buffer.toString());
//                builder.setTitle("Tasks list");
//                builder.show();
//            }
//        });
    }
}