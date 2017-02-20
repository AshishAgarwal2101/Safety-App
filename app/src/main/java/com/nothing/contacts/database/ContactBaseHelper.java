package com.nothing.contacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nothing.contacts.database.ContactsScheme.ContactsTable;

public class ContactBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "contactBase.db";

    public ContactBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ ContactsTable.NAME+"("+
                "_id integer primary key autoincrement, "+
                ContactsTable.cols.UUID+", "+
                ContactsTable.cols.NAME+", "+
                ContactsTable.cols.NUMBER+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
