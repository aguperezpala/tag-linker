package com.sury.taglinker;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;

public class ContentHandler {
	// The data base used to retrieve/save the data
	private DBHandler db;
	// The main hash table to do a fast look of the elements
	private SparseArray<Element> elementsHash = new SparseArray<Element>();
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
		
		Contact result = new Contact((int)rep.id);
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
	
	Tag transformTag(DBHandler.TagRep tagrep) {
		if (tagrep == null) {
			return null;
		}
		
		Tag result = new Tag((int)tagrep.id);
		result.setDescription(tagrep.description);
		result.setName(tagrep.name);
		
		return result;
	}
	
	
	// @brief Load and initialize all the contacts into memory.
	//		  This method will also fill the arrays and hash tables. We will
	//		  load also the ContactInformation of each contact
	// @throws the associated exception.
	//
	public void loadContacts() throws TagLinkerException {
		this.contacts.clear();
		this.contactInfos.clear();
		
		// load all the contacts from the DB
		try {
			ArrayList<DBHandler.ContactRep> reps = db.getAllContacts();
			if (reps == null) {
				return;
			}
			// for each contact representation we will create the associated
			// contact and load its contact information
			//
			for (DBHandler.ContactRep r : reps) {
				Contact c = transformContact(r);
				
				// put the contact into the hash and list
				this.elementsHash.put(c.getID(), c);
				this.contacts.add(c);
				
				// now get the contact info for this contact
				ArrayList<DBHandler.ContactInfoRep> cireps = db.getContactInfoReps(c.getID());
				for (DBHandler.ContactInfoRep cirep : cireps) {
					ContactInfo ci = new ContactInfo(c);
					ci.setData(cirep.data);
					ci.setType(cirep.type);
					
					// add this contact info to the contact itself
					c.addContactInfo(ci);
					
					// add it to the list
					this.contactInfos.add(ci);
				}
			}	
			
		} catch(Exception e) {
			throw new TagLinkerException(e.toString());
		}
	}
	
	// @brief Load the Tags into memory and fill all the data structures, we
	//		  will clear all the old data before.
	//		  This method will also load the TagEntry's elements.
	// @throw on error
	//
	public void loadTags() throws TagLinkerException {
		// clear the old data
		this.tagEntrys.clear();
		this.tags.clear();
		
		// try to get all the information from the DB
		try {
			ArrayList<DBHandler.TagRep> reps = db.getAllTags();
			if (reps == null) {
				return;
			}
			
			// for each contact representation we will create the associated
			// contact and load its contact information
			//
			for (DBHandler.TagRep r : reps) {
				Tag t = transformTag(r);
				
				// put the tag into the hash and list
				this.elementsHash.put(t.getID(), t);
				this.tags.add(t);
				
				// now get the tagEntry for this tag
				ArrayList<DBHandler.TagEntryRep> tereps = db.getTagEntries(t.getID());
				for (DBHandler.TagEntryRep terep : tereps) {
					// we need first obtain the associated contact to this tagEntry
					// if we haven't then something is really bad here and we
					// will need to fix the data base.
					// for now we will throw an exception?
					Element contact = this.elementsHash.get((int)terep.contactID);
					
					if (contact == null) {
						Log.v("ERROR", "Incopatible DB: we have a problem with a tag:" +
								"The tag " + t.getName() + " has" +
								" a tagEntry linked to a inexistent contact with id" +
								String.valueOf(terep.contactID));
						// continue with the next element
						continue;
//						throw new TagLinkerException("The tag " + t.getName() + " has" +
//								" a tagEntry linked to a inexistent contact with id" +
//								String.valueOf(terep.contactID));
					}
					
					// check if it is the correct type
					if (contact.getType() != ElementType.CONTACT) {
						Log.v("ERROR", "We tag an element that is not a contact!" +
								" This is very rare! The type is: " + 
								String.valueOf(ElementType.toInt(contact.getType())) +
								" for the TagID:" + String.valueOf(t.getID()));
						continue;
					}
					
					TagEntry te = new TagEntry(t, (Contact) contact);
					te.setDescription(terep.description);
					
					// add it to the list
					this.tagEntrys.add(te);
				}
			}	
			
		} catch(Exception e) {
			throw new TagLinkerException(e.toString());
		}
	}
	
	// @brief Load all the links and fill the structures. We first will destroy
	//		  the old data.
	// @throws on error
	//
	public void loadLinks() throws TagLinkerException {
		this.links.clear();
		
		// load all the links from the DB
		try {
			ArrayList<DBHandler.LinkRep> reps = db.getAllLinks();
			if (reps == null) {
				return;
			}
			// for each link representation we will create the associated
			// link and search for the other two associated elements
			//
			for (DBHandler.LinkRep r : reps) {
				// first get the two elements we need to construct the link
				Element first = this.elementsHash.get((int)r.firstID);
				Element second = this.elementsHash.get((int)r.secondID);
				
				if (first == null || second == null) {
					Log.v("ERROR", "Some of the elements needed by the link " + 
							r.description + ", is not in the DB... Some of the " +
							"next ID's are missing: " + String.valueOf(r.firstID) +
							", or: " + String.valueOf(r.secondID));
					continue;
				}
				
				// now that we have the two elements, we will create the link
				Link link = new Link(first, second);
				link.setDescription(r.description);
				link.setType(r.type);
				
				// put the link into list
				this.links.add(link);
			}	
			
		} catch(Exception e) {
			throw new TagLinkerException(e.toString());
		}
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
	// @throw Exception on error
	//
	public void init() throws TagLinkerException {
		// first remove all the data
		this.elementsHash.clear();
		
		// now try to load everything from the DB
		assert(this.db != null);
		
		// we will load things in the next order:
		loadContacts();
		loadTags();
		loadLinks();
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
		contact.setID((int)cr.id);
		
		// add the contact to the list and hash
		this.elementsHash.put(contact.getID(), contact);
		this.contacts.add(contact);
		
		return true;		
	}	
	
	// @brief Get the number of contacts we have in the DB
	// @return the number of contacts we have in the data base
	//
	public int getNumContacts() {
		return this.contacts.size();		
	}
	
	// @brief Get a specific contact from a given ID.
	// @param id		The id of the element we want to retrieve
	// @return the allocated contact | null on error.
	//
	public Contact getContact(int id) {
		// we assume that we have all the data already loaded, note that we
		// are not supporting having part of the data in memory and other part
		// in the DB.
		//
		Element contact = this.elementsHash.get(id);
		if (contact == null) {
			return null;
		}
		// check if the type of the element is a Contact Type
		if (contact.getType() != ElementType.CONTACT) {
			Log.v("ERROR", "The type of the element " + String.valueOf(id) +
					" is not of type contact!");
			return null;
		}
		
		return (Contact) contact;
	}
	
	// @brief Get all the contacts in the DB.
	// @return the allocated list of contacts on success | null on error
	//
	public List<Contact> getAllContacts() {
		// we simply return the list of all the local contacts
		return this.contacts;		
	}
	
	// @brief Update an already created contact.
	// @param contact 	The contact to be updated. If the element doesn't exists
	//				  	on the DB this method will return false
	// @return true on success | false otherwise
	//
	public boolean updateContact(Contact contact) {
		assert(contact != null);
		
		// here we need to do some things before update the contact itself.
		// 1) Check if the contact argument is the same we currently have, if not
		//	  it is not possible to update it.
		// 2) If we have it, then update it in the DB.
		//
		
		Element cnt = this.elementsHash.get(contact.getID());
		if (cnt != contact) {
			Log.v("ERROR", "We are trying to update an inexistent contact?");
			return false;
		}
		
		// we have that contact already, update the db then
		assert(db != null);
		DBHandler.ContactRep crep = transformContact(contact);
		
		return db.updateContact(crep);		
	}
	
	// @brief Remove a already created contact.
	// @param contact	The contact to be removed. If the element donesn't exists
	//					this method will return false.
	// @return true on success | false otherwise.
	//
	public boolean removeContact(Contact contact) {
		assert(contact != null);
		
		// Note that if we remove a contact we need to remove also:
		// 1) All the TagEntries associated to that contact.
		// 2) All the Links where one of them is linked to the Contact.
		// 3) All the ContactInfo associated to the contact.
		// We need to remove all this from the DB and also from our current
		// Data (list and hash).
		
		// check if we have that element
		Element cnt = this.elementsHash.get(contact.getID());
		if (cnt != contact) {
			Log.v("ERROR", "We are trying to update an inexistent contact?");
			return false;
		}
		
		// First we will remove everything from the DB
		// remove the contact from the DB
		DBHandler.ContactRep crep = transformContact(contact);
		if (!db.removeContact(contact.getID())) {
			Log.v("ERROR", "When trying to remove the contact:" + contact.getName() +
					", and ID: " + String.valueOf(contact.getID()));
			return false;
		}
		int contactInfoEntries = db.removeContactInfoEntries(contact.getID());
		int tagEntries = db.removeTagEntriesFromContact(contact.getID());
		int linkEntries = db.removeLinkFromElement(contact.getID());
		
		// now we have to remove the elements here and check if we remove
		// the same amount of data at least (it will be better to check if the
		// amount of data was the same content that we are just removing now).
		//
		ArrayList<ContactInfo> cielements = contact.getContactInfoList();
		if (cielements.size() != contactInfoEntries) {
			Log.v("ERROR", "We have different number of elements between the DB and" +
					" the loaded data. DB ContactInfo elements: " + 
					String.valueOf(contactInfoEntries) + ", Mem ContactInfo: " +
					String.valueOf(cielements.size()));
		}
		this.contactInfos.removeAll(cielements);
		
		// remove all the tags
		ArrayList<TagEntry> tagEntriesToRemove = new ArrayList<TagEntry>();
		for (TagEntry te : this.tagEntrys) {
			if (te.getContact() == contact) {
				// we need to remove this tag entry
				tagEntriesToRemove.add(te);
			}
		}
		if (tagEntriesToRemove.size() != tagEntries) {
			Log.v("ERROR", "We have different number of elements between the DB and" +
					" the loaded data. DB TagEntries elements: " + 
					String.valueOf(tagEntries) + ", Mem TagEntries: " +
					String.valueOf(tagEntriesToRemove.size()));
		}
		// this is slow... 
		this.tagEntrys.removeAll(tagEntriesToRemove);
		
		// now we will remove all the links
		ArrayList<Link> linksToRemove = new ArrayList<Link>();
		for (Link l : this.links) {
			if (l.getFirst() == contact || l.getSecond() == contact) {
				// we need to remove this tag entry
				linksToRemove.add(l);
			}
		}
		if (linksToRemove.size() != linkEntries) {
			Log.v("ERROR", "We have different number of elements between the DB and" +
					" the loaded data. DB LinkEntries elements: " + 
					String.valueOf(linkEntries) + ", Mem LinkEntries: " +
					String.valueOf(linksToRemove.size()));
		}
		this.links.removeAll(linksToRemove);
		
		// now we will remove the contact from the lists and hash
		this.elementsHash.remove(contact.getID());
		this.contacts.remove(contact.getID());
		
		return true;
	}
	
	
	
	// @brief Create a new contact info entry. 
	// @param ci	The contactInfo to be created with the information 
	//				already set in the structure.
	// @return true on success | false otherwise
	//
	public boolean createContactInfo(ContactInfo ci) {
		assert(ci != null);
		
		// check if we have the contact associated
		Contact contact = ci.getContact();
		if (contact == null || this.elementsHash.get(contact.getID()) == null) {
			Log.v("ERROR", "The associated contact to the ContactInfo doesn't " +
					"exists");
			return false;
		}
		
		// now we need to add the contact info into the DB and then to the
		// local data
		DBHandler.ContactInfoRep cirep = db.new ContactInfoRep();
		cirep.contactID = contact.getID();
		cirep.data = ci.getData();
		cirep.type = ci.getType();
		if (!db.createContactInfo(cirep)) {
			Log.v("ERROR", "Error creating the contact info into the DB");
			return false;
		}
		
		// Now create and associate the Contact info into the local data
		this.contactInfos.add(ci);
		contact.addContactInfo(ci);
		// TODO: probably it will be a good idea check if the ci already exists
		// in the current contact
		
		return true;
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
	public ArrayList<ContactInfo> getAllContactInfo(int cid) {
		
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
	public Tag getTag(int id) {
		
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
	public ArrayList<TagEntry> getAllTagEntry(int tid) {
		
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
	public Link getLink(int id) {
		
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
