package com.stepan.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

@Entity
public class User implements UserDetails{

	@NotNull
	@Size(min=5, max=255)
	@Pattern(regexp = "(\\d|\\w|\\.)+\\@(\\d|\\w|\\.)+\\.(\\d|\\w)+",  message = "example: john.berg@gmail.com")
	private String userName;
	
	@NotNull
	@Size(min=7, max=255)
	private String password;

	@Id
	@GeneratedValue
	private Long id;
		
	@OneToOne(mappedBy="user", fetch= FetchType.EAGER)
	private Role role;
	
	public User() {}

	public User(String email, String password) {
		this.userName = email;
		this.password = password;
	}
	
	public String getPassword() { return password; }

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() { return "User [email=" + userName + ", id=" + id+ "]"; }

	
	// Userdetails:
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}