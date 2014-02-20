package com.sury.taglinker;


public enum ElementType {
	UNKNOWN,	// 0
	CONTACT, 	// 1
	TAG,		// 2
	LINK;		// 3
	
	public static int toInt(ElementType type) {
		switch (type) {
		case UNKNOWN:
			return 0;
		case CONTACT:
			return 1;
		case TAG:
			return 2;
		case LINK:
			return 3;
		default:
			// error?
			return 0;	
		}
	}
}