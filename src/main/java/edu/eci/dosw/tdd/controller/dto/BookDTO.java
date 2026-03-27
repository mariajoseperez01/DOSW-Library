package edu.eci.dosw.tdd.controller.dto;

import lombok.Data;

@Data
public class BookDTO {
	private String id;
	private String title;
	private String author;
	private Integer totalUnits;
	private Integer availableUnits;
	private Boolean available;
}
