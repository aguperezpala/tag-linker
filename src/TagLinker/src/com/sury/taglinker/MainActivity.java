package com.sury.taglinker;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.deleteDatabase("TagLinker");
//        return;
        
        DBHandler db = new DBHandler(this);
        
        /**
         */
        // Inserting Contacts
        Log.v("Insert: ", "Inserting ..");
        db.createContact(db.new ContactRep("agustin", 
        								"Esta es la descripcion",
        								1,
        								"Persona",
        								"Argentina"));
        db.createContact(db.new ContactRep("Constanza", 
				"Esta es la descripcion de la cony",
				26,
				"Persona",
				"Cordoba"));
         
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts.."); 
        ArrayList<DBHandler.ContactRep> contacts = db.getAllContacts();
        if (contacts == null) {
        	Log.v("DEBUG", "Error here");
        	return;
        }
        for (DBHandler.ContactRep cn : contacts) {
            String log = "Id: "+cn.id+" ,Name: " + cn.name + " ,Desc: " + cn.description +
            		", Age: " + String.valueOf(cn.age) + ", Kind: " + cn.kind + 
            		", Location: " + cn.location;
                // Writing Contacts to log
            Log.d("Name: ", log);
        }
        
        // REMOVE ALL, this for debug
//        db.deleteDB();
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        editText.setText("Sending..." + message);
    }
    
}
