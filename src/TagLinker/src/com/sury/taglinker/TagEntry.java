package com.sury.taglinker;

public class TagEntry {
	private Tag tag;
	private Contact contact;
	private String description;
	
	// To build TagEntry's we need a tag and a contact as requirement
	//
	TagEntry(Tag tag, Contact contact) {
		this.tag = tag;
		this.contact = contact;
	}
	
	// Setters
	//
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	// Getters
	//
	public Tag getTag() {
		return this.tag;
	}
	public Contact getContact() {
		return this.contact;
	}
	public String getDescription() {
		return this.description;
	}

}
