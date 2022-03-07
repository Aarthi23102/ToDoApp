package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    static int inde=0;
    public DBHelper(@Nullable Context context) {
        super(context, "TasksDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE taskstable(id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS taskstable");
        onCreate(db);
    }

    public boolean insertTask(String text)
    {
        inde+=1;
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
//        contentValues.put("id",id);
        contentValues.put("task",text);
        long res=db.insert("taskstable",null,contentValues);





        if(res==-1)
            return false;
        return true;
    }

    public boolean updateTask(int id, String text)
    {
        db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("id",id);
        cv.put("task",text);
        Cursor cur=db.rawQuery("select * from taskstable where id=?",new String[]{String.valueOf(id)});
        if(cur.getCount()>0)
        {
            long res=db.update("taskstable",cv,"id=?",new String[]{String.valueOf(id)});
            if(res==-1)
                return false;
            return true;
        }
        return false;
    }

    public boolean deleteTask(int id)
    {
        db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from taskstable where id=?", new String[]{String.valueOf(id)});
        if(cur.getCount()>0)
        {
            long res=db.delete("taskstable","id=?",new String[]{String.valueOf(id)});
            if(res==-1)
                return false;
            return true;
        }
        return false;
    }

    public Cursor getTasks()
    {
        db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from taskstable",null);
        return cur;
    }
}