package com.sury.taglinker;

public class Link {
	// Element references that this link "link"
	private Element first;
	private Element second;
	private String type;
	private String description;
	
	// Constructor: We need the two elements to build this links
	//
	public Link(Element first, Element second) {
		this.first = first;
		this.second = second;
	}
	
	// Setters
	//
	public void setDescription(String desc) {
		this.description = desc;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	// Getters
	//
	public Element getFirst() {
		return this.first;
	}
	public Element getSecond() {
		return this.second;
	}
	public String getType() {
		return this.type;
	}
	public String getDescription() {
		return this.description;
	}
}
