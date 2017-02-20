package com.nothing.contacts.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nothing.contacts.ContactDetails;
import com.nothing.contacts.database.ContactsScheme.ContactsTable;

import java.util.UUID;

public class ContactCursorWrapper extends CursorWrapper{

    public ContactCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public ContactDetails getContact(){
        String UUIDSring = getString(getColumnIndex(ContactsTable.cols.UUID));
        String number = getString(getColumnIndex(ContactsTable.cols.NUMBER));
        String name = getString(getColumnIndex(ContactsTable.cols.NAME));

        ContactDetails cont = new ContactDetails(UUID.fromString(UUIDSring));
        cont.setName(name);
        cont.setNumber(number);
        return cont;
    }
}
