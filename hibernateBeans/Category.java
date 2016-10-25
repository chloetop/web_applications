/**
 * 
 */
package edu.unsw.comp9321.hibernateBeans;

/**
 * @author Millyto
 * 
 */
public enum Category {
	ARTS, BOOKS, CLOTHING, COMPUTERS,	ELECTRONICS, GAMES, SHOES, SPORTS;

	public static Category getEnumBySubstring(String value) {

		value = value.toUpperCase();
		for (Category v : values()) {
			if (v.name().contains(value))
				return v;
		}
		return null;
	}
}
