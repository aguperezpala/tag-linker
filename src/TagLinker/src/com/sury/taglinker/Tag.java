package com.sury.taglinker;

public class Tag extends Element {
	
	// Local variables
	private String name;
	private String description;
	
	// Constructors
	//
	Tag() {
		super(-1, ElementType.TAG);
	}
	Tag(int id) {
		super(id, ElementType.TAG);
	}
	
	// Setters
	//
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	// Getters
	//
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}

}
