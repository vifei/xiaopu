package com.example.xiaopu.exam;

import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBService {
   private SQLiteDatabase db;
   public DBService(){
      db=SQLiteDatabase.openDatabase("/data/data/com.example.xiaopu/databases/mydatabase.db", null, SQLiteDatabase.OPEN_READONLY);
   }
   @SuppressLint("Range")
   public List<Question> getQuestions(){
      List<Question> list=new ArrayList<Question>();
      Cursor cursor=db.rawQuery("select * from test", null);
      if(cursor.getCount()>0){
         cursor.moveToFirst();
         int count=cursor.getCount();
         for(int i=0;i<count;++i){
            cursor.moveToPosition(i);
            Question question=new Question();
            question.setTestcontent(cursor.getString(cursor.getColumnIndex("testcontent")));
            question.setTestid(cursor.getInt(cursor.getColumnIndex("testid")));
            question.setAnswera(cursor.getString(cursor.getColumnIndex("answera")));
            question.setAnswerb(cursor.getString(cursor.getColumnIndex("answerb")));
            question.setSelectedAnswer(-1);
            list.add(question);
         }
      }
      return list;
   }
}
