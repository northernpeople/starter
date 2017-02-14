package com.stepan.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

@Entity
public class User{

	@NotNull
	@Size(min=5, max=255)
	@Pattern(regexp = "(\\d|\\w|\\.)+\\@(\\d|\\w|\\.)+\\.(\\d|\\w)+",  message = "example: john.berg@gmail.com")
	private String email;
	
	@NotNull
	@Size(min=7, max=255)
	private String password;

	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne(mappedBy="user")
	private Role role;

	public User() {}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Long getId() {
		return id;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	/**
	 * Creates bidirectional link with new role.
	 * Overrides existing role.
	 * @param r
	 */
	public void linkNewRole(Role r){
		Assert.notNull(r);
		Assert.notNull(this.getId(), "user must have persistent identity to be linked with new role");
		Assert.notNull(r.getId(), "role must have persistent identity to be linked with user");
		this.role = r;
		r.setUser(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", id=" + id
				+ "]";
	}
}