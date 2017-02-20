package com.nothing.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    int plus;
    private Toolbar toolbar;
    private Button button1,button2,button3,button4,button5;
    private TextView name1,name2,name3,name4,name5, number1,number2,number3,number4,number5;
    private String name, number;
    private List<ContactDetails> conts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        conts = ContactsLab.get(getApplicationContext()).getContacts();

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        button5 = (Button)findViewById(R.id.button5);
        name1 = (TextView)findViewById(R.id.name1);
        name2 = (TextView)findViewById(R.id.name2);
        name3 = (TextView)findViewById(R.id.name3);
        name4 = (TextView)findViewById(R.id.name4);
        name5 = (TextView)findViewById(R.id.name5);
        number1 = (TextView)findViewById(R.id.number1);
        number2 = (TextView)findViewById(R.id.number2);
        number3 = (TextView)findViewById(R.id.number3);
        number4 = (TextView)findViewById(R.id.number4);
        number5 = (TextView)findViewById(R.id.number5);

        final Intent pickContacts = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button1.getText().toString().equals("ADD")) {
                    startActivityForResult(pickContacts, 1);
                    //button1.setText("REMOVE");
                }
                else{
                    conts = ContactsLab.get(getApplicationContext()).deleteContact(number1.getText().toString());
                    name1.setText("name");
                    number1.setText("number");
                    button1.setText("ADD");
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button2.getText().toString().equals("ADD")) {
                    startActivityForResult(pickContacts, 2);
                    //button2.setText("REMOVE");
                }
                else{
                    conts = ContactsLab.get(getApplicationContext()).deleteContact(number2.getText().toString());
                    name2.setText("name");
                    number2.setText("number");
                    button2.setText("ADD");
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button3.getText().toString().equals("ADD")) {
                    startActivityForResult(pickContacts, 3);
                    //button3.setText("REMOVE");
                }
                else{
                    conts = ContactsLab.get(getApplicationContext()).deleteContact(number3.getText().toString());
                    name3.setText("name");
                    number3.setText("number");
                    button3.setText("ADD");
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button4.getText().toString().equals("ADD")) {
                    startActivityForResult(pickContacts, 4);
                    //button4.setText("REMOVE");
                }
                else{
                    conts = ContactsLab.get(getApplicationContext()).deleteContact(number4.getText().toString());
                    name4.setText("name");
                    number4.setText("number");
                    button4.setText("ADD");
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button5.getText().toString().equals("ADD")) {
                    startActivityForResult(pickContacts, 5);
                    //button5.setText("REMOVE");
                }
                else{
                    conts = ContactsLab.get(getApplicationContext()).deleteContact(number5.getText().toString());
                    name5.setText("name");
                    number5.setText("number");
                    button5.setText("ADD");
                }
            }
        });

        updateText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.about){
            Intent intent = new Intent(ContactsActivity.this, IntroActivity.class);
            startActivity(intent);
            save();
            return true;
        }
        if (id==android.R.id.home){
            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", plus = 0);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(data!=null){

            Uri contactUri = data.getData();
            String queryFields[] = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c = getContentResolver().query(contactUri, queryFields, null, null, null);
            try{
                if(c.getCount()==0){
                    return;
                }
                c.moveToFirst();
                name = c.getString(0);
                number = c.getString(1);
                newContact(name, number);
                //selectContactButton.setText(name);
                //numberButton.setText(number);
                //updateText();
                //names[i] = name;
                //numbers[i] = number;
                //i++;

            }finally{
                c.close();
            }

            switch(requestCode){
                case 1:
                    name1.setText(name);
                    number1.setText(number);
                    button1.setText("REMOVE");
                    break;
                case 2:
                    name2.setText(name);
                    number2.setText(number);
                    button2.setText("REMOVE");
                    break;
                case 3:
                    name3.setText(name);
                    number3.setText(number);
                    button3.setText("REMOVE");
                    break;
                case 4:
                    name4.setText(name);
                    number4.setText(number);
                    button4.setText("REMOVE");
                    break;
                case 5:
                    name5.setText(name);
                    number5.setText(number);
                    button5.setText("REMOVE");
                    break;
            }
        }
    }

    private void updateText(){
        int k=1;

        button1.setText("ADD");
        name1.setText("name");
        number1.setText("number");
        button2.setText("ADD");
        name2.setText("name");
        number2.setText("number");
        button3.setText("ADD");
        name3.setText("name");
        number3.setText("number");
        button4.setText("ADD");
        name4.setText("name");
        number4.setText("number");
        button5.setText("ADD");
        name5.setText("name");
        number5.setText("number");

        for(ContactDetails cont:conts){
            name = cont.getName();
            number = cont.getNumber();
            switch(k){
                case 1:
                    name1.setText(name);
                    number1.setText(number);
                    button1.setText("REMOVE");
                    k++;
                    break;
                case 2:
                    name2.setText(name);
                    number2.setText(number);
                    button2.setText("REMOVE");
                    k++;
                    break;
                case 3:
                    name3.setText(name);
                    number3.setText(number);
                    button3.setText("REMOVE");
                    k++;
                    break;
                case 4:
                    name4.setText(name);
                    number4.setText(number);
                    button4.setText("REMOVE");
                    k++;
                    break;
                case 5:
                    name5.setText(name);
                    number5.setText(number);
                    button5.setText("REMOVE");
                    k++;
                    break;
            }
        }

    }
    private void newContact(String name, String number){
        ContactDetails cont1 = new ContactDetails();
        cont1.setName(name);
        cont1.setNumber(number);
        conts = ContactsLab.get(getApplicationContext()).addContact(cont1);
    }
}
