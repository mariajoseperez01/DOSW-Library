package edu.eci.dosw.tdd.persistence.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserDAO {

	@Id
	@Column(name = "id", nullable = false, length = 50)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
