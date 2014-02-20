package com.sury.taglinker;

public class ContactInfo {
	private Contact contact;
	private String type;
	private String data;
	
	
	// Constructor:
	// @param contact 	We need the contact ref to build this class
	//
	public ContactInfo(Contact contact) {
		this.contact = contact;
	}
	
	// Setters
	//
	public void setType(String type) {
		this.type = type;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	// Getters
	//
	public Contact getContact() {
		return this.contact;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getData() {
		return this.data;
	}
}
