package com.nothing.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nothing.contacts.database.ContactBaseHelper;
import com.nothing.contacts.database.ContactCursorWrapper;
import com.nothing.contacts.database.ContactsScheme.ContactsTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactsLab {

    private static ContactsLab sContactsLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ContactsLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ContactBaseHelper(mContext).getWritableDatabase();
    }

    public static ContactsLab get(Context context){
        if(sContactsLab == null){
            sContactsLab = new ContactsLab(context);
        }
        return sContactsLab;
    }

    public List<ContactDetails> addContact(ContactDetails cont){
        ContentValues values = getContentValues(cont);
        mDatabase.insert(ContactsTable.NAME, null, values);
        return getContacts();
    }

    public List<ContactDetails> deleteContact(String number){
        mDatabase.delete(ContactsTable.NAME, ContactsTable.cols.NUMBER+" = ?", new String[]{number});
        return getContacts();
    }

    public ContactDetails getContact(UUID id){
        ContactCursorWrapper cursor = queryContacts(ContactsTable.cols.UUID+" = ?", new String[]{id.toString()});

        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getContact();
        }finally {
            cursor.close();
        }
    }
    public List<ContactDetails> getContacts(){
        List<ContactDetails> conts = new ArrayList<>();

        ContactCursorWrapper cursor = queryContacts(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                conts.add(cursor.getContact());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return conts;
    }

    public void updateContact(ContactDetails cont){
        String UUIDString = cont.getId().toString();
        ContentValues values = getContentValues(cont);

        mDatabase.update(ContactsTable.NAME, values, ContactsTable.cols.UUID+" = ?", new String[]{UUIDString});
    }

    private static ContentValues getContentValues(ContactDetails cont){
        ContentValues values = new ContentValues();
        values.put(ContactsTable.cols.UUID, cont.getId().toString());
        values.put(ContactsTable.cols.NAME, cont.getName());
        values.put(ContactsTable.cols.NUMBER, cont.getNumber());
        return values;
    }

    private ContactCursorWrapper queryContacts(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ContactsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ContactCursorWrapper(cursor);
    }

}
