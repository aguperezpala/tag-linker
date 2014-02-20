package com.sury.taglinker;

import java.util.ArrayList;
import java.util.Hashtable;

public class ContentHandler {
	// The data base used to retrieve/save the data
	private DBHandler db;
	// The main hash table to do a fast look of the elements
	private Hashtable<long, Element> elementsHash = new Hashtable<long, Element>();
	// The container of all the elements by type
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
	ArrayList<Tag> tags = new ArrayList<Tag>();
	ArrayList<TagEntry> tagEntrys = new ArrayList<TagEntry>();
	ArrayList<Link> links = new ArrayList<Link>();
	
	
	
	
	////////////////////////////////////////////////////////////////////////////
	// private methods
	//
	
	// Transformer methods
	//
	Contact transformContact(DBHandler.ContactRep rep) {
		if (rep == null) {
			return null;
		}
		
		Contact result = new Contact(rep.id);
		result.setName(rep.name);
		result.setDescription(rep.description);
		result.setAge(rep.age);
		result.setKind(rep.kind);
		result.setLocation(rep.location);
		
		return result;
	}
	DBHandler.ContactRep transformContact(Contact contact) {
		if (contact == null) {
			return null;
		}
		
		DBHandler.ContactRep result = db.new ContactRep();
		result.id = contact.getID();
		result.description = contact.getDescription();
		result.age = contact.getAge();
		result.kind = contact.getKind();
		result.name = contact.getName();
		
		return result;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////
	// Public methods
	//
	
	
	// Constructor needs a DBHandler to work with
	//
	public ContentHandler(DBHandler dataBase) {
		assert(db != null);
		this.db = dataBase;
	}
	
	
	////////////////////////////////////////////////////////////////////////////
	//						API used by the application						  //
	////////////////////////////////////////////////////////////////////////////
	
	// @brief Initialize the ContentHandler. This method will load all the data 
	//		  from the DB and fill all the containers.
	// @return true on success | false on error
	//
	public boolean init() {
		// first remove all the data
		this.elementsHash.clear();
		this.contacts.clear();
		this.contactInfos.clear();
		this.tags.clear();
		this.tagEntrys.clear();
		this.links.clear();
		
		// now try to load everything from the DB
		assert(this.db != null);
		
		// we will load things in the next order
	}
		
	
	////////////////////////////////////////////////////////////////////////////
	// Contacts handling
	
	// @brief Create a new contact. 
	//		  This method will discard the id of the Contact and will assign a 
	//		  new one to the contact passed in the args.
	// @param contact	The contact to be created with the information already
	//					set in the structure.
	// @return true on success | false otherwise
	//
	public boolean createContact(Contact contact) {
		assert(contact != null);
		assert(db != null);
		
		// we will create a new contact calling the DB Handler
		DBHandler.ContactRep cr = db.new ContactRep();
		cr.description = contact.getDescription();
		cr.age = contact.getAge();
		cr.kind = contact.getKind();
		cr.name = contact.getName();
		
		// try to create the contact
		if (!db.createContact(cr)) {
			return false;
		}
		
		// we could create it, assign the new id to the contact
		contact.setID(cr.id);
		return true;		
	}	
	
	// @brief Get the number of contacts we have in the DB
	// @return the number of contacts we have in the data base
	//
	public int getNumContacts() {
		assert(db != null);
		return db.getNumContacts();		
	}
	
	// @brief Get a specific contact from a given ID.
	// @param id		The id of the element we want to retrieve
	// @return the allocated contact | null on error.
	//
	public Contact getContact(long id) {
		assert(db != null);
		
		DBHandler.ContactRep rep = db.getContact(id);
		if (rep == null) {
			return null;
		}
		// construct the new contact
		Contact result = new Contact(id);
		result.setName(rep.name);
		result.setDescription(rep.description);
		result.setAge(rep.age);
		result.setKind(rep.kind);
		result.setLocation(rep.location);
		return result;
	}
	
	// @brief Get all the contacts in the DB.
	// @return the allocated list of contacts on success | null on error
	//
	public ArrayList<Contact> getAllContacts() {
		assert(db != null);
		
		ArrayList<DBHandler.ContactRep> reps = db.getAllContacts();
		if (reps == null) {
			return null;
		}
		
		ArrayList<Contact> result = new ArrayList<Contact>();
		for (DBHandler.ContactRep r : reps) {
			Contact c = new Contact(r.id);
			
			c.setName(r.name);
			c.setDescription(r.description);
			c.setAge(r.age);
			c.setKind(r.kind);
			c.setLocation(r.location);
			result.add(c);
		}
		
		return result;
	}
	
	// @brief Update an already created contact.
	// @param contact 	The contact to be updated. If the element doesn't exists
	//				  	on the DB this method will return false
	// @return true on success | false otherwise
	//
	public boolean updateContact(Contact contact) {
		
	}
	
	// @brief Remove a already created contact.
	// @param contact	The contact to be removed. If the element donesn't exists
	//					this method will return false.
	// @return true on success | false otherwise.
	//
	public boolean removeContact(Contact contact) {
		
	}
	
	
	
	// @brief Create a new contact info entry. 
	// @param ci	The contactInfo to be created with the information 
	//				already set in the structure.
	// @return true on success | false otherwise
	//
	public boolean createContactInfo(ContactInfo ci) {
		
	}	
	
	// @brief Get the number of contactInfo entries we have in the DB
	// @return the number of contactInfo entries we have in the data base
	//
	public int getNumContactInfoEntries() {
		
	}
		
	// @brief Get all the ContactInfo entries in the DB from a given Contact ID.
	// @param cid	The id of the contact we want its entries
	// @return the allocated list of ContactInfo on success | null on error
	//
	public ArrayList<ContactInfo> getAllContactInfo(long cid) {
		
	}
	
	// @brief Update an already created ContactInfo.
	// @param ci 	The ContactInfo to be updated. If the element doesn't exists
	//				  	on the DB this method will return false
	// @return true on success | false otherwise
	//
	public boolean updateContactInfo(ContactInfo ci) {
		
	}
	
	// @brief Remove a already created ContactInfo.
	// @param ci	The ContactInfo to be removed. If the element donesn't exists
	//				this method will return false.
	// @return true on success | false otherwise.
	//
	public boolean removeContactInfo(ContactInfo ci) {
		
	}
	
	
	
	
	////////////////////////////////////////////////////////////////////////////
	// Tags handling
	
	// @brief Create a new Tag. 
	//		  This method will discard the id of the Tag and will assign a 
	//		  new one to the Tag passed in the args.
	// @param tag	The Tag to be created with the information already
	//				set in the structure.
	// @return true on success | false otherwise
	//
	public boolean createTag(Tag tag) {
		
	}	
	
	// @brief Get the number of Tags we have in the DB
	// @return the number of Tags we have in the data base
	//
	public int getNumTags() {
		
	}
	
	// @brief Get a specific Tag from a given ID.
	// @param id		The id of the element we want to retrieve
	// @return the allocated Tag | null on error.
	//
	public Tag getTag(long id) {
		
	}
	
	// @brief Get all the Tags in the DB.
	// @return the allocated list of Tags on success | null on error
	//
	public ArrayList<Tag> getAllTags() {
		
	}
	
	// @brief Update an already created Tag.
	// @param tag 	The Tag to be updated. If the element doesn't exists
	//				on the DB this method will return false
	// @return true on success | false otherwise
	//
	public boolean updateTag(Tag tag) {
		
	}
	
	// @brief Remove a already created Tag.
	// @param tag	The Tag to be removed. If the element donesn't exists
	//				this method will return false.
	// @return true on success | false otherwise.
	//
	public boolean removeTag(Tag tag) {
		
	}
	
	
	
	// @brief Create a new TagEntry. 
	// @param te	The TagEntry to be created with the information 
	//				already set in the structure.
	// @return true on success | false otherwise
	//
	public boolean createTagEntry(TagEntry te) {
		
	}	
	
	// @brief Get the number of TagEntry entries we have in the DB
	// @return the number of TagEntry entries we have in the data base
	//
	public int getNumTagEntryEntries() {
		
	}
		
	// @brief Get all the TagEntry entries in the DB from a given Tag ID.
	// @param tid	The id of the tag we want its entries
	// @return the allocated list of TagEntry on success | null on error
	//
	public ArrayList<TagEntry> getAllTagEntry(long tid) {
		
	}
	
	// @brief Update an already created TagEntry.
	// @param te 	The TagEntry to be updated. If the element doesn't exists
	//				  	on the DB this method will return false
	// @return true on success | false otherwise
	//
	public boolean updateTagEntry(TagEntry te) {
		
	}
	
	// @brief Remove a already created TagEntry.
	// @param te	The TagEntry to be removed. If the element donesn't exists
	//				this method will return false.
	// @return true on success | false otherwise.
	//
	public boolean removeTagEntry(TagEntry te) {
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	// Links handling
	//
	
	
	// @brief Create a new Link. 
	//		  This method will discard the id of the Link and will assign a 
	//		  new one to the Link passed in the args.
	// @param link	The Tag to be created with the information already
	//				set in the structure.
	// @return true on success | false otherwise
	//
	public boolean createLink(Link link) {
		
	}	
	
	// @brief Get the number of Links we have in the DB
	// @return the number of Links we have in the data base
	//
	public int getNumLinks() {
		
	}
	
	// @brief Get a specific Link from a given ID.
	// @param id		The id of the element we want to retrieve
	// @return the allocated Link | null on error.
	//
	public Link getLink(long id) {
		
	}
	
	// @brief Get all the Links in the DB.
	// @return the allocated list of Links on success | null on error
	//
	public ArrayList<Link> getAllLinks() {
		
	}
	
	// @brief Update an already created Link.
	// @param tag 	The Link to be updated. If the element doesn't exists
	//				on the DB this method will return false
	// @return true on success | false otherwise
	//
	public boolean updateLink(Link tag) {
		
	}
	
	// @brief Remove a already created Link.
	// @param link	The Tag to be removed. If the element donesn't exists
	//				this method will return false.
	// @return true on success | false otherwise.
	//
	public boolean removeLink(Link link) {
		
	}
	
}
