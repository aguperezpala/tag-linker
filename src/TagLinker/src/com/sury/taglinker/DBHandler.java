package com.sury.taglinker;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

// @note This module is now implemented as one class only. This should be improved
// 		 later with a better design. Now we will implement the "abstract" API
//		 so it will be easier to change the functionality later.
// 		 We can implement different kind of DB like XML, online services, sql or
//		 whatever we want
//		 Check google code issue #2
//


public class DBHandler extends SQLiteOpenHelper {
	////////////////////////////////////////////////////////////////////////////
	// Members
	//
	// Static variables
    // DB version, we will use just in case we want to upgrade something later
	// and know how to handle the data.
    private static final int DATABASE_VERSION = 1;
 
    // DB Name
    private static final String DATABASE_NAME = "TagLinker";
 
    // The tables of the db
    private static final String TABLE_ELEMENTS = "Elements";
    private static final String TABLE_CONTACTS = "Contacts";
    private static final String TABLE_CONTACTS_INFO = "ContactsInfo";
    private static final String TABLE_TAGS = "Tags";
    private static final String TABLE_TAGS_ENTRY = "TagsEntry";
    private static final String TABLE_LINKS = "Links";
 
    // Table Columns for each table, this is all in the same place, ugly
    // but we will do it this way now
    //
    // Elements
    private static final String KEY_ELEMENTS_ID = "id";
    private static final String KEY_ELEMENTS_TYPE = "type";
    // Contacts
    private static final String KEY_CONTACTS_ID = "id";
    private static final String KEY_CONTACTS_NAME = "name";
    private static final String KEY_CONTACTS_DESC = "description";
    private static final String KEY_CONTACTS_AGE = "age";
    private static final String KEY_CONTACTS_KIND = "kind";
    private static final String KEY_CONTACTS_LOCATION = "location";
    // ContactInfo
    private static final String KEY_CONTACTINF_CID = "cid";
    private static final String KEY_CONTACTINF_TYPE = "type";
    private static final String KEY_CONTACTINF_DATA = "data";
    // Tags
    private static final String KEY_TAGS_ID = "id";
    private static final String KEY_TAGS_NAME = "name";
    private static final String KEY_TAGS_DESC = "description";
    // TagEntry
    private static final String KEY_TAGSENTRY_TID = "tid";
    private static final String KEY_TAGSENTRY_CID = "cid";
    private static final String KEY_TAGSENTRY_DESC = "description";
    // Links
    private static final String KEY_LINKS_ID = "id";
    private static final String KEY_LINKS_FIRSTID = "firstID";
    private static final String KEY_LINKS_SECONDID = "secondID";
    private static final String KEY_LINKS_DESC = "description";
    private static final String KEY_LINKS_TYPE = "type";
	
	////////////////////////////////////////////////////////////////////////////
	// Representations
	// @note We will use this representations to handle abstract the type of 
    //		 elements used in other parts of the application.
    //		 This is not very efficient, or, I have no idea if java will handle
    //		 all these copies very well.
    //		 An improvement in the design will be to move each of this tables/reps
    //		 in different classes and handle each one differently
    //
	
	// Contact tables
	public class ContactRep {
		public long id;
		public String name;
		public String description;
		public int age;
		public String kind;
		public String location;
		
		// Constructors
		public ContactRep(){}
		public ContactRep(String name, 
						  String description,
						  int age,
						  String kind,
						  String location) {
			this.name = name;
			this.description = description;
			this.age = age;
			this.kind = kind;
			this.location = location;
		}
	}
	public class ContactInfoRep {
		public long contactID;
		public String type;
		public String data;
		
		// constructors
		public ContactInfoRep(){}
		public ContactInfoRep(long contactID, String type, String data) {
			this.contactID = contactID;
			this.type = type;
			this.data = data;
		}
	}
	
	// Tag tables
	public class TagRep {
		public long id;
		public String name;
		public String description;
		
		public TagRep(){}
		public TagRep(String name, String description){
			this.name = name;
			this.description = description;
		}
	}
	public class TagEntryRep {
		public long tagID;
		public long contactID;
		public String description;
		
		public TagEntryRep(){}
		public TagEntryRep(long tagID, long contactID, String desc){
			this.tagID = tagID;
			this.contactID = contactID;
			this.description = desc;
		}
	}
	
	// Link table
	public class LinkRep {
		public long id;
		public long firstID;
		public long secondID;
		public String description;
		public String type;
		
		public LinkRep(){}
		public LinkRep(long firstID, long secondID, String description, String type) {
			this.firstID = firstID;
			this.secondID = secondID;
			this.description = description;
			this.type = type;
		}
	}
	
	public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// @note here: That using the INTEGER PRIMARY KEY we are just saying that
    	//		 we want to use an integer autoincrement type for this value.
    	//
    	
    	// Create table elements string
        String CREATE_ELEMENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ELEMENTS + "("
                + KEY_ELEMENTS_ID + " INTEGER PRIMARY KEY," + 
                KEY_ELEMENTS_TYPE + " INTEGER)";
        
        // Create the Contacts table
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                + KEY_CONTACTS_ID + " INTEGER PRIMARY KEY," + 
                KEY_CONTACTS_NAME + " TEXT," +
                KEY_CONTACTS_DESC + " TEXT," +
                KEY_CONTACTS_AGE + " INT," +
                KEY_CONTACTS_KIND + " STRING," +
                KEY_CONTACTS_LOCATION + " TEXT" +                
                ")";
        
        // Create the Contact Info table
        String CREATE_CONTACTS_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS_INFO + "("
                + KEY_CONTACTINF_CID + " INTEGER PRIMARY KEY," + 
                KEY_CONTACTINF_TYPE + " TEXT," +
                KEY_CONTACTINF_DATA + " TEXT" +
                ")";
        
        // Create the Tags table
        String CREATE_TAGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TAGS + "("
                + KEY_TAGS_ID + " INTEGER PRIMARY KEY," + 
                KEY_TAGS_NAME + " TEXT," +
                KEY_TAGS_DESC + " TEXT" +
                ")";
        
        // Create table TagEntry
        String CREATE_TAGSENTRY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TAGS_ENTRY + "("
            + KEY_TAGSENTRY_TID + " INTEGER," + 
            KEY_TAGSENTRY_CID + " INTEGER," +
            KEY_TAGSENTRY_DESC + " TEXT" +
            ")";
        // Create table Links
        String CREATE_LINKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LINKS + "("
                + KEY_LINKS_ID + " INTEGER PRIMARY KEY," + 
                KEY_LINKS_FIRSTID + " INTEGER," +
                KEY_LINKS_SECONDID + " INTEGER," +
                KEY_LINKS_DESC + " STRING," +
                KEY_LINKS_TYPE + " TEXT" +                
                ")";
        
        // create all the tables now
        db.execSQL(CREATE_ELEMENTS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONTACTS_INFO_TABLE);
        db.execSQL(CREATE_TAGS_TABLE);
        db.execSQL(CREATE_TAGSENTRY_TABLE);
        db.execSQL(CREATE_LINKS_TABLE);
        
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
 
        // Create tables again
        onCreate(db);
    }

	
	
	////////////////////////////////////////////////////////////////////////////
	// 								API methods
	////////////////////////////////////////////////////////////////////////////
	
    
	////////////////////////////////////////////////////////////////////////////
	// Modify / Create / Delete methods
	//
    
    // @brief This private method will create a new element and will return
    //		  the new ID to that element.
    //		  This method will open the DB so be sure you don't have already
    //		  opened the db
    // @return the new id on success | <= 0 on error.
    //
    private long createElement(int type) {
    	SQLiteDatabase db = this.getWritableDatabase();
   	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_ELEMENTS_TYPE, type); // Contact Name
	 
	    // Inserting Row
	    long result = db.insert(TABLE_ELEMENTS, null, values);
	    db.close(); // Closing database connection
	    
    	return result;
    }
    
    // @brief Remove an element from a given id
    // @return true on success | false otherwise
    private boolean removeElement(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ELEMENTS, KEY_ELEMENTS_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        
    	return result >= 0;
    }
	
    // Contact and contact info
    //
    
    // @brief Create a new entry for contacts from a given ContactRepresentation
    // @param c		The contact representation
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean createContact(ContactRep c) {
    	if (c == null) {
    		assert (c != null);
    		return false;
    	}
    	
    	// first we need to create a new entry in the Elements table
    	long elementID = createElement(ElementType.toInt(ElementType.CONTACT));
    	if (elementID <= 0) {
    		Log.v("DEBUG", "We couldn't create a elementID" + String.valueOf(elementID));
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// now we know that we create a new row, so we will use the new ID
    	// for this element
	    ContentValues values = new ContentValues();
	    c.id = elementID;
	    values.put(KEY_CONTACTS_ID, elementID);
	    values.put(KEY_CONTACTS_NAME, c.name);
	    values.put(KEY_CONTACTS_DESC, c.description);
	    values.put(KEY_CONTACTS_AGE, c.age);
	    values.put(KEY_CONTACTS_KIND, c.kind);
	    values.put(KEY_CONTACTS_LOCATION, c.location);
	 
	    // Inserting Row
	    long result = db.insert(TABLE_CONTACTS, null, values);
	    db.close(); // Closing database connection
    	return result >= 0;
    }
    // @brief Create a new entry for contactInfo from a given ContactInfoRep
    // @param ci	The contactInfo representation
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean createContactInfo(ContactInfoRep ci) {
    	if (ci == null) {
    		assert (ci != null);
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// Create the values for this contact info
    	// @note that we will not check for any constraint here
    	// 	 	 and probably we should! defensive programming? :(
    	//
	    ContentValues values = new ContentValues();
	    values.put(KEY_CONTACTINF_CID, ci.contactID);
	    values.put(KEY_CONTACTINF_DATA, ci.data);
	    values.put(KEY_CONTACTINF_TYPE, ci.type);
	    
	    // Inserting Row
	    long result = db.insert(TABLE_CONTACTS_INFO, null, values);
	    db.close(); // Closing database connection
    	return result >= 0;
    }
    
    // @brief Update an already created contact. This will update all the fields
    //		  of the contact.
    //		  If the entry doesn't exists this will return an error.
    // @param c		The contact representation to be updated
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean updateContact(ContactRep c) {
    	if (c == null) {
    		assert (c != null);
    		return false;
    	}
    	
        SQLiteDatabase db = this.getWritableDatabase();

        // set the values
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACTS_ID, c.id);
	    values.put(KEY_CONTACTS_NAME, c.name);
	    values.put(KEY_CONTACTS_DESC, c.description);
	    values.put(KEY_CONTACTS_AGE, c.age);
	    values.put(KEY_CONTACTS_KIND, c.kind);
	    values.put(KEY_CONTACTS_LOCATION, c.location);
     
	    // updating row
	    int result = db.update(TABLE_CONTACTS, values, KEY_CONTACTS_ID + " = ?",
                new String[] { String.valueOf(c.id) });
	    db.close();
        return result > 0;
    }
    
    // @brief Update an already created contact info. This will update all the fields
    //		  of the contactInfo.
    //		  If the entry doesn't exists this will return an error.
    // @param co	The contactInfo representation to be updated
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean updateContactInfo(ContactInfoRep ci) {
    	if (ci == null) {
    		assert (ci != null);
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
 	    values.put(KEY_CONTACTINF_CID, ci.contactID);
 	    values.put(KEY_CONTACTINF_DATA, ci.data);
 	    values.put(KEY_CONTACTINF_TYPE, ci.type);
 	    
 	    // TODO: here we have not an unique key so we could have a problem that
 	    // 		 we don't want. It could be the case we remove more than
 	    // 		 one row at the same type
 	    //
 	    // updating row
        int result = db.update(TABLE_CONTACTS_INFO, values, 
        		KEY_CONTACTINF_CID + " = ?" +
        		" AND " +
        		KEY_CONTACTINF_TYPE + " = ? " +
        		" AND " +
        		KEY_CONTACTINF_DATA + " = ? ",
                new String[] { String.valueOf(ci.contactID), ci.type, ci.data });
        
        db.close();
        
        return result > 0;
    }
    
    // @brief The destruction methods will remove the instance from the DB
    // 		  but it will not remove the associated elements like links nor
    //	      tags nor any other element that is related with this contact.
    //		  Is the caller responsibility to do that.
    //		  If you try to remove an inexistent element this method will
    //		  return false
    // @param id	The id of the element to be removed
    // @return true on success | false otherwise.
    // @note No exceptions handling here
    //
    public boolean removeContact(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	// we should first remove the contact in the element table
    	
        int result = db.delete(TABLE_ELEMENTS, KEY_ELEMENTS_ID + " = ?",
                new String[] { String.valueOf(id) });
        if (result <= 0) {
        	// the element didn't exists?
        	Log.v("DEBUG", "Trying to remove an element with an invalid ID: " +
        			String.valueOf(id));
        	db.close();
        	return false;
        }
        
        // now try to remove it from the table contact
        result = db.delete(TABLE_CONTACTS, KEY_CONTACTS_ID + " = ?", 
        		new String[] { String.valueOf(id) });
        if (result <= 0) {
        	// the element exist but wasn't in the contact table?
        	// it is another type of element?
        	Log.v("DEBUG", "We remove an element with id " + String.valueOf(id) +
        			" but that wasn't a contact element?.....!");
        	db.close();
        	return false;
        }
        
        // everything fine
        db.close();
        return result > 0;
    }
    
    // @brief The destruction methods will remove the instance from the DB
    // 		  but it will not remove the associated elements like links nor
    //	      tags nor any other element that is related with this contact.
    //		  Is the caller responsibility to do that.
    //		  If you try to remove an inexistent element this method will
    //		  return false
    // @param ci	The Contact info representation to be removed
    // @return true on success | false otherwise.
    // @note No exceptions handling here
    //
    public boolean removeContactInfo(ContactInfoRep ci) {
    	SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CONTACTS_INFO,
        		KEY_CONTACTINF_CID + " = ?" +
        		" AND " +
        		KEY_CONTACTINF_TYPE + " = ? " +
        		" AND " +
        		KEY_CONTACTINF_DATA + " = ? ",
                new String[] { String.valueOf(ci.contactID), ci.type, ci.data });
        db.close();
        return result > 0;
    }
    
    // @brief Remove a contact info from a given Contact ID
    // @param id	The ID we want to use to remove the entries
    // @return the number of entries removed
    // @note No exceptions handling here
    //
    public int removeContactInfoEntries(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CONTACTS_INFO,
        		KEY_CONTACTINF_CID + " = ?",
                new String[] { String.valueOf(id)});
        db.close();
        return result;
    }
    
    // Tags queries
    //
    
    // @brief Create a new entry for tags from a given TagRepresentation
    // @param t		The tag representation
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean createTag(TagRep t) {
    	if (t == null) {
    		assert (t != null);
    		return false;
    	}
    	
    	// first we need to create a new entry in the Elements table
    	long elementID = createElement(ElementType.toInt(ElementType.TAG));
    	if (elementID <= 0) {
    		Log.v("DEBUG", "We couldn't create a elementID" + String.valueOf(elementID));
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();

    	// now we know that we create a new row, so we will use the new ID
    	// for this element
	    ContentValues values = new ContentValues();
	    t.id = elementID;
	    values.put(KEY_TAGS_ID, elementID);
	    values.put(KEY_TAGS_NAME, t.name);
	    values.put(KEY_TAGS_DESC, t.description);
	 
	    // Inserting Row
	    long result = db.insert(TABLE_TAGS, null, values);
	    db.close(); // Closing database connection
    	return result >= 0;
    }
    
    // @brief Create a new tagEntry from a given TagEntryRepresentation
    // @param te	The tagEntry representation
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean createTagEntry(TagEntryRep te) {
    	if (te == null) {
    		assert (te != null);
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// Create the values for this TagEntry info
	    ContentValues values = new ContentValues();
	    values.put(KEY_TAGSENTRY_CID, te.contactID);
	    values.put(KEY_TAGSENTRY_TID, te.tagID);
	    values.put(KEY_TAGSENTRY_DESC, te.description);
	    
	    // Inserting Row
	    long result = db.insert(TABLE_TAGS_ENTRY, null, values);
	    db.close(); // Closing database connection
    	return result >= 0;
    }
    
    // @brief Update an already created tag. If the tag doesn't exists this method
    // 		  will return false.
    //		  All the fields will be updated.
    // @param t		The representation
    // @return true on success | false otherwise
    // @note No exceptions are handled here
    //
    public boolean updateTag(TagRep t) {
    	SQLiteDatabase db = this.getWritableDatabase();

    	// now we know that we create a new row, so we will use the new ID
    	// for this element
	    ContentValues values = new ContentValues();
	    values.put(KEY_TAGS_ID, t.id);
	    values.put(KEY_TAGS_NAME, t.name);
	    values.put(KEY_TAGS_DESC, t.description);
	    
	 // updating row
        int result = db.update(TABLE_TAGS, values, KEY_TAGS_ID + " = ?",
                new String[] { String.valueOf(t.id) });
        
        db.close();
        return result  > 0;
    }
    
    // @brief Update an already created tag entry. If the tag entry doesn't 
    // 		  exists this method will return false.
    //		  All the fields will be updated.
    // @param te	The representation
    // @return true on success | false otherwise
    // @note No exceptions are handled here
    //
    public boolean updateTagEntry(TagEntryRep te) {
    	if (te == null) {
    		assert te != null;
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();

    	// now we know that we create a new row, so we will use the new ID
    	// for this element
	    ContentValues values = new ContentValues();
	    values.put(KEY_TAGSENTRY_CID, te.contactID);
	    values.put(KEY_TAGSENTRY_TID, te.tagID);
	    values.put(KEY_TAGSENTRY_DESC, te.description);
	    
	    // updating row
        int result = db.update(TABLE_TAGS, values, KEY_TAGSENTRY_TID + " = ?" + 
        				 " AND " +
        				 KEY_TAGSENTRY_CID + " = ?",
                new String[] { String.valueOf(te.tagID), String.valueOf(te.contactID) });
        
        db.close();
        return result > 0;
    }
    
    // @brief The destruction methods will remove the instance from the DB
    // 		  but it will not remove the associated elements like links nor
    //	      tags nor any other element that is related with this contact.
    //		  Is the caller responsibility to do that.
    //		  If you try to remove an inexistent element this method will
    //		  return false
    // @param id	The id of the element to be removed
    // @return true on success | false otherwise.
    // @note No exceptions handling here
    //
    public boolean removeTag(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// we should first remove the contact in the element table    	
        int result = db.delete(TABLE_ELEMENTS, KEY_ELEMENTS_ID + " = ?",
                new String[] { String.valueOf(id) });
        if (result <= 0) {
        	// the element didn't exists?
        	Log.v("DEBUG", "Trying to remove an element with an invalid ID: " +
        			String.valueOf(id));
        	db.close();
        	return false;
        }
        
        // now try to remove it from the table tag
        result = db.delete(TABLE_TAGS, KEY_TAGS_ID + " = ?", 
        		new String[] { String.valueOf(id) });
        if (result <= 0) {
        	// the element exist but wasn't in the tags table?
        	// it is another type of element?
        	Log.v("DEBUG", "We remove an element with id " + String.valueOf(id) +
        			" but that wasn't a tags element?.....!");
        	db.close();
        	return false;
        }
        
        // everything fine?
        db.close();
        return result > 0;
    }
    
    // @brief The destruction methods will remove the instance from the DB
    // 		  but it will not remove the associated elements like links nor
    //	      tags nor any other element that is related with this contact.
    //		  Is the caller responsibility to do that.
    //		  If you try to remove an inexistent element this method will
    //		  return false
    // @param te	The tag entry to be removed
    // @return true on success | false otherwise.
    // @note No exceptions handling here
    //
    public boolean removeTagEntry(TagEntryRep te) {
    	if (te == null) {
    		assert te != null;
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
        // now try to remove it from the table tag
        int result = db.delete(TABLE_TAGS_ENTRY, 
        		KEY_TAGSENTRY_TID + " = ?" + 
				 " AND " +
				 KEY_TAGSENTRY_CID + " = ?",
				 new String[] { String.valueOf(te.tagID), String.valueOf(te.contactID) });
              
        // everything fine?
        db.close();
        return result > 0;
    }
    
    // @brief Remove TagEntry's from a given Tag ID
    // @param id	The ID we want to use to remove the entries
    // @return the number of entries removed
    // @note No exceptions handling here
    //
    public int removeTagEntriesFromTag(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
        // now try to remove it from the table tag
        int result = db.delete(TABLE_TAGS_ENTRY, KEY_TAGSENTRY_TID + " = ?", 
        		new String[] { String.valueOf(id)});
              
        // everything fine?
        db.close();
        return result;
    }
    
    // @brief Remove TagEntry's from a given Contact ID
    // @param id	The ID we want to use to remove the entries
    // @return the number of entries removed
    // @note No exceptions handling here
    //
    public int removeTagEntriesFromContact(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
        // now try to remove it from the table tag
        int result = db.delete(TABLE_TAGS_ENTRY, KEY_TAGSENTRY_CID + " = ?", 
        		new String[] { String.valueOf(id)});
              
        // everything fine?
        db.close();
        return result;
    }
    
    // @brief Create a new entry for links from a given LinkRepresentation
    // @param l		The representation
    // @return true on success | false otherwise
    // @note No exception handling here.
    //
    public boolean createLink(LinkRep l) {
    	if (l == null) {
    		assert l != null;
    		return false;
    	}
    	
    	// first we need to create a new entry in the Elements table
    	long elementID = createElement(ElementType.toInt(ElementType.LINK));
    	if (elementID <= 0) {
    		Log.v("DEBUG", "We couldn't create a elementID" + String.valueOf(elementID));
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// now we know that we create a new row, so we will use the new ID
    	// for this element
	    ContentValues values = new ContentValues();
	    values.put(KEY_LINKS_ID, elementID);
	    values.put(KEY_LINKS_DESC, l.description);
	    values.put(KEY_LINKS_FIRSTID, l.firstID);
	    values.put(KEY_LINKS_SECONDID, l.secondID);
	    values.put(KEY_LINKS_TYPE, l.type);
	 
	    // Inserting Row
	    long result = db.insert(TABLE_LINKS, null, values);
	    db.close(); // Closing database connection
    	return result >= 0;
    }
    
    // @brief Update an already created link entry. If the link entry doesn't 
    // 		  exists this method will return false.
    //		  All the fields will be updated.
    // @param l		The representation
    // @return true on success | false otherwise
    // @note No exceptions are handled here
    //
    public boolean updateLink(LinkRep l) {
    	if (l == null) {
    		assert l != null;
    		return false;
    	}
    	
    	SQLiteDatabase db = this.getWritableDatabase();

    	// now we know that we create a new row, so we will use the new ID
    	// for this element
	    ContentValues values = new ContentValues();
	    values.put(KEY_LINKS_ID, l.id);
	    values.put(KEY_LINKS_DESC, l.description);
	    values.put(KEY_LINKS_FIRSTID, l.firstID);
	    values.put(KEY_LINKS_SECONDID, l.secondID);
	    values.put(KEY_LINKS_TYPE, l.type);
	    
	 // updating row
        int result =  db.update(TABLE_LINKS, values, KEY_LINKS_ID + " = ?",
                new String[] { String.valueOf(l.id) });
        
        db.close();
        return result > 0;
    }
    
    // @brief The destruction methods will remove the instance from the DB
    // 		  but it will not remove the associated elements like links nor
    //	      tags nor any other element that is related with this contact.
    //		  Is the caller responsibility to do that.
    //		  If you try to remove an inexistent element this method will
    //		  return false
    // @param id 	The id of the element to be removed
    // @return true on success | false otherwise.
    // @note No exceptions handling here
    //
    public boolean removeLink(long id) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	// we should first remove the contact in the element table    	
        int result = db.delete(TABLE_ELEMENTS, KEY_ELEMENTS_ID + " = ?",
                new String[] { String.valueOf(id) });
        if (result <= 0) {
        	// the element didn't exists?
        	Log.v("DEBUG", "Trying to remove an element with an invalid ID: " +
        			String.valueOf(id));
        	db.close();
        	return false;
        }
        
        // now try to remove it from the table tag
        result = db.delete(TABLE_LINKS, KEY_LINKS_ID + " = ?", 
        		new String[] { String.valueOf(id) });
        if (result <= 0) {
        	// the element exist but wasn't in the tags table?
        	// it is another type of element?
        	Log.v("DEBUG", "We remove an element with id " + String.valueOf(id) +
        			" but that wasn't a links element?.....!");
        	db.close();
        	return false;
        }
        
        // everything fine?
        db.close();
        return result > 0;
    }
    
    // @brief Remove the links where one of the elements is a given ID
    // @param id 	The id used to match with some of the elements of the link
    // @return the number of links removed
    // @note No exceptions handling here
    //
    public int removeLinkFromElement(long id) {
    	// first get all the elements that matches with the given ID
	    String selectQuery = "SELECT  * FROM " + TABLE_LINKS + " WHERE " +
	    		KEY_LINKS_FIRSTID + " = " + String.valueOf(id) + " OR " +
	    		KEY_LINKS_SECONDID + " = " + String.valueOf(id);;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<LinkRep> lreps = new ArrayList<LinkRep>();
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	LinkRep lrep = new LinkRep();
	        	lrep.id = Integer.parseInt(cursor.getString(0));	            
	    	    // Adding contact to list
	    	    lreps.add(lrep);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    db.close();
	    cursor.close();
	    
	    // now we will remove one by one ... VERY SLOW THIS!
	    int result =  0;
	    for (LinkRep lrep : lreps) {
	    	if (this.removeLink(lrep.id)) {
	    		result++;
	    	}
	    }
	    
	    return result;
    }
    
	////////////////////////////////////////////////////////////////////////////
	// Query methods
	//
		
	// Contact and ContactInfo representations queries
	//
	
	// @brief Get the number of contacts we have
    // @return the number of contacts
    // @note No exceptions handling here
	//
	public int getNumContacts() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        db.close();
        return cursor.getCount();
	}
	
	// @brief Get all the contact IDS
	// TODO
	
	// @brief Get an specific Contact from a given ID
	// @return the associated representation on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public ContactRep getContact(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    Cursor cursor = db.query(TABLE_CONTACTS, 
	    		new String[] { KEY_CONTACTS_ID, KEY_CONTACTS_NAME, KEY_CONTACTS_DESC,
	    		KEY_CONTACTS_AGE, KEY_CONTACTS_KIND, KEY_CONTACTS_LOCATION}, 
	    		KEY_CONTACTS_ID + "=?",
	            new String[] { String.valueOf(id) }, 
	            null, null, null, null);
	    
	    ContactRep cr = null;
	    
	    if (cursor == null) {
	    	db.close();
	    	return cr;
	    }
	    cursor.moveToFirst();
	    
	    cr = new ContactRep();
	    cr.id = Integer.parseInt(cursor.getString(0));
	    cr.name = cursor.getString(1);
	    cr.description = cursor.getString(2);
	    cr.age = Integer.parseInt(cursor.getString(3));
	    cr.kind = cursor.getString(4);
	    cr.location = cursor.getString(5);
	    
	    assert (cr.id == id);
	    
	    // return contact
	    db.close();
	    cursor.close();
	    return cr;
	}
	
	// @brief Get a list of Contacts from a given list of IDS
	// TODO
	
	// @brief Get all the contacts we have from the DB.
	// @return the associated list of representations on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public ArrayList<ContactRep> getAllContacts() {
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<ContactRep> crs = null;
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
    		crs = new ArrayList<ContactRep>();
	    	
	        do {
	            ContactRep cr = new ContactRep();
	    	    cr.id = Integer.parseInt(cursor.getString(0));
	    	    cr.name = cursor.getString(1);
	    	    cr.description = cursor.getString(2);
	    	    cr.age = Integer.parseInt(cursor.getString(3));
	    	    cr.kind = cursor.getString(4);
	    	    cr.location = cursor.getString(5);
	            // Adding contact to list
	    	    crs.add(cr);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    db.close();
	    cursor.close();
	    return crs;
	}
	
	// @brief Get the number of contactInfo entries we have
    // @return the number of contacts
    // @note No exceptions handling here
	//
	public int getNumContactInfo() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        db.close();
        return cursor.getCount();
	}
	
	// @brief Get all the contactInfo entries we have from the DB.
	// @return the associated list of representations on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public ArrayList<ContactInfoRep> getContactInfoReps(long cid) {
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS_INFO;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<ContactInfoRep> cirs = null;
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
    		cirs = new ArrayList<ContactInfoRep>();
	    	
	        do {
	        	ContactInfoRep cr = new ContactInfoRep();
	    	    cr.contactID = Integer.parseInt(cursor.getString(0));
	    	    cr.type = cursor.getString(1);
	    	    cr.data = cursor.getString(2);
	            // Adding contactInfo to list
	    	    cirs.add(cr);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    db.close();
	    cursor.close();
	    return cirs;
	}
	
	
	// Tag and TagEntry representations queries
	//
	
	// @brief Get the number of tags we have
    // @return the number of tags
    // @note No exceptions handling here
	//
	public int getNumTags() {
		String countQuery = "SELECT  * FROM " + TABLE_TAGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        db.close();
        return cursor.getCount();
	}
	
	// @brief Get all the Tag IDS
	// TODO
	
	// @brief Get an specific Tag from a given ID
	// @return the associated representation on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public TagRep getTag(long tid) {
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    Cursor cursor = db.query(TABLE_TAGS, 
	    		new String[] { KEY_TAGS_ID, KEY_TAGS_NAME, KEY_TAGS_DESC}, 
	    		KEY_TAGS_ID + "=?",
	            new String[] { String.valueOf(tid) }, 
	            null, null, null, null);
	    
	    TagRep trp = null;
	    
	    if (cursor == null) {
	    	db.close();
	    	return trp;
	    }
	    trp = new TagRep();
	    cursor.moveToFirst();
	    
	    trp.id = Integer.parseInt(cursor.getString(0));
	    trp.name = cursor.getString(1);
	    trp.description = cursor.getString(2);

	    assert (trp.id == tid);
	    
	    // return contact
	    db.close();
	    cursor.close();
	    return trp;
	}
	
	// @brief Get a list of Tags from a list of IDS
	// TODO
	
	// @brief Get all the tags we have from the DB.
	// @return the associated list of representations on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public ArrayList<TagRep> getAllTags() {
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TAGS;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<TagRep> treps = null;
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
    		treps = new ArrayList<TagRep>();
	    	
	        do {
	        	TagRep tr = new TagRep();
	    	    tr.id = Integer.parseInt(cursor.getString(0));
	    	    tr.name = cursor.getString(1);
	    	    tr.description = cursor.getString(2);
	            
	    	    // Adding tag to list
	    	    treps.add(tr);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    db.close();
	    cursor.close();
	    return treps;
	}
	
	// @brief Get the number of tagEntry entries we have
    // @return the number of tagEntry entries
    // @note No exceptions handling here
	//
	public int getNumTagEntrys() {
		String countQuery = "SELECT  * FROM " + TABLE_TAGS_ENTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        db.close();
        return cursor.getCount();
	}
	
	// @brief Get all the tagEntry entries we have from the DB.
	// @return the associated list of representations on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public ArrayList<TagEntryRep> getTagEntries(long tid) {
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TAGS_ENTRY;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<TagEntryRep> tel = null;
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
    		tel = new ArrayList<TagEntryRep>();
	    	
	        do {
	        	TagEntryRep tr = new TagEntryRep();
	    	    tr.tagID = Integer.parseInt(cursor.getString(0));
	    	    tr.contactID = Integer.parseInt(cursor.getString(1));
	    	    tr.description = cursor.getString(2);
	            
	    	    // Adding tag to list
	    	    tel.add(tr);
	        } while (cursor.moveToNext());
	    }
	 
	    // return tag list
	    db.close();
	    cursor.close();
	    return tel;
	}
	
	
	// Link queries
	//
	
	// @brief Get the number of links we have
    // @return the number of links
    // @note No exceptions handling here
	//
	public int getNumLinks() {
		String countQuery = "SELECT  * FROM " + TABLE_LINKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        db.close();
        return cursor.getCount();
	}
	
	// @brief Get all the Link IDS
	// TODO
	
	// @brief Get an specific Link from a given ID
	// @return the associated representation on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public LinkRep getLink(long lid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TAGS, 
	    		new String[] { KEY_LINKS_ID, KEY_LINKS_FIRSTID, KEY_LINKS_SECONDID,
	    					   KEY_LINKS_DESC, KEY_LINKS_TYPE}, 
	    		KEY_LINKS_ID + "=?",
	            new String[] { String.valueOf(lid) }, 
	            null, null, null, null);
		
		LinkRep lrep = null;
	    
	    if (cursor == null) {
	    	db.close();
	    	return lrep;
	    }
	    cursor.moveToFirst();
	    
	    lrep = new LinkRep();
	    lrep.id = Integer.parseInt(cursor.getString(0));
	    lrep.firstID = Integer.parseInt(cursor.getString(1));
	    lrep.secondID = Integer.parseInt(cursor.getString(2));
	    lrep.description = cursor.getString(3);
	    lrep.type = cursor.getString(4);

	    assert (lrep.id == lid);
	    
	    // return contact
	    db.close();
	    cursor.close();
	    return lrep;
	}
	
	// @brief Get all the links we have from the DB.
	// @return the associated list of representations on success | null element 
	//		   otherwise
	// @note No error handling here.
	//
	public ArrayList<LinkRep> getAllLinks() {
		// Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TAGS_ENTRY;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<LinkRep> lreps = null;
	    
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
    		lreps = new ArrayList<LinkRep>();
	    	
	        do {
	        	LinkRep lrep = new LinkRep();
	        	lrep.id = Integer.parseInt(cursor.getString(0));
	    	    lrep.firstID = Integer.parseInt(cursor.getString(1));
	    	    lrep.secondID = Integer.parseInt(cursor.getString(2));
	    	    lrep.description = cursor.getString(3);
	    	    lrep.type = cursor.getString(4);
	            
	    	    // Adding tag to list
	    	    lreps.add(lrep);
	        } while (cursor.moveToNext());
	    } 
	 
	    // return contact list
	    db.close();
	    cursor.close();
	    return lreps;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////
	// Destroy all the tables of the DB, this is probably useful for debug
	//
	
	public boolean deleteDB() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ELEMENTS, null, null);
		db.delete(TABLE_CONTACTS, null, null);
		db.delete(TABLE_CONTACTS_INFO, null, null);
		db.delete(TABLE_TAGS, null, null);
		db.delete(TABLE_TAGS_ENTRY, null, null);
		db.delete(TABLE_LINKS, null, null);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELEMENTS);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS_INFO);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_ENTRY);
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKS);
		
		db.close();
		return true;
	}
	
	
	
}
