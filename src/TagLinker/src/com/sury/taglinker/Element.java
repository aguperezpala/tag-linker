package com.sury.taglinker;



public class Element {
	// the id of the element
	private int id = 0;
	// the type of the element
	private ElementType type = ElementType.UNKNOWN;
	
	
	// Constructor
	//
	public Element(int ID, ElementType eType) {
		id = ID;
		type = eType;
	}
	
	// Setters
	//
	public void setID(int id) {
		this.id = id;
	}
	
	// Getters
	//
	public int getID() {
		return id;
	}
	public ElementType getType() {
		return type;
	}
}
