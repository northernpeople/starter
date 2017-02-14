package com.stepan.model;

import javax.persistence.*;

@Entity
public class Role {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	
	@OneToOne
	private User user;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
