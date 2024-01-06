package com.example.xiaopu.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.xiaopu.bean.Talks;

import java.util.ArrayList;
import java.util.List;

public class TalksDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TalksSQLite.db";
    private static final String TABLE_NAME_NOTE = "Talks";

    private static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME_NOTE + " (id integer primary key, type integer, content text, uid integer)";


    public TalksDbOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertData(Talks talks) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", talks.getId());
        values.put("type", talks.getType());
        values.put("content", talks.getContent());
        values.put("uid", talks.getUid());

        return db.insert(TABLE_NAME_NOTE, null, values);
    }

    public List<Talks> queryAllFromDb() {

        SQLiteDatabase db = getWritableDatabase();
        List<Talks> talksList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String uid = cursor.getString(cursor.getColumnIndex("uid"));

                Talks note = new Talks();
                note.setId(id);
                note.setType(type);
                note.setContent(content);
                note.setUid(uid);

                talksList.add(note);
            }
            cursor.close();
        }

        return talksList;

    }

    public List<Talks> queryAllFromDbByUid(String myuid) {

        SQLiteDatabase db = getWritableDatabase();
        List<Talks> talksList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME_NOTE, null, "uid == ?", new String[]{myuid}, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String uid = cursor.getString(cursor.getColumnIndex("uid"));

                Talks note = new Talks();
                note.setId(id);
                note.setType(type);
                note.setContent(content);
                note.setUid(uid);

                talksList.add(note);
            }
            cursor.close();
        }

        return talksList;

    }


    public void deleteByUid(String myuid) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_NOTE, "uid=?", new String[]{myuid});
    }


}