package edu.eci.dosw.tdd.core.model;

import java.util.Objects;

public class User {

	private String name;
	private String password;
	private Role role;
	private String id;

	public User() {
	}

	public User(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public User(String name, String password, Role role, String id) {
		this.name = name;
		this.password = password;
		this.role = role;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
