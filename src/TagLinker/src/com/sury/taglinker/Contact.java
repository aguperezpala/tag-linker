package com.sury.taglinker;

import java.util.ArrayList;

public class Contact extends Element {
	private String name;
	private String description;
	private int age;
	private String kind;
	private String location;
	private ArrayList<ContactInfo> contactInfoList = null;
	
	// Constructors
	//
	public Contact() {
		super(-1, ElementType.CONTACT);
	}
	public Contact(int id) {
		super(id, ElementType.CONTACT);
	}
	
	// Setters
	//
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	public void setAge(int age){
		this.age = age;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	
	// Add a new ContactInfo element assigned to this one
	//
	public void addContactInfo(ContactInfo cinfo) {
		if (this.contactInfoList == null) {
			contactInfoList = new ArrayList<ContactInfo>();
		}
		this.contactInfoList.add(cinfo);
	}
	
	// Remove a particular element in the list
	// @returns true if we could remove the element | false otherwise
	//
	public boolean removeContactInfo(ContactInfo cinfo) {
		return (this.contactInfoList != null) && 
				this.contactInfoList.remove(cinfo);
	}
	
	
	// Getters
	//
	public String getName() {
		return this.name;
	}
	public String getDescription() {
		return this.description;
	}
	public int getAge() {
		return this.age;
	}
	public String getLocation() {
		return this.location;
	}
	public String getKind() {
		return this.kind;
	}
	
	public ArrayList<ContactInfo> getContactInfoList() {
		return this.contactInfoList;
	}
	
}
