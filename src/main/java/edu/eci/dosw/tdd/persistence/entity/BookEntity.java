package edu.eci.dosw.tdd.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @Column(nullable = false, updatable = false, length = 64)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "total_units", nullable = false)
    private int totalUnits;

    @Column(name = "available_units", nullable = false)
    private int availableUnits;
}
