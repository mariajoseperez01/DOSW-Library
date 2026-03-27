package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class Book {
	private String id;
	private String title;
	private String author;
	private int totalUnits;
	private int availableUnits;
}
