package com.galere.pictures.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {
	
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "Login")
	@Size(max = 50)
	private String login;
	
	@Column(name = "Pass")
	@Size(max = 50)
	private String pass;

	@Column(name = "Entry")
	private LocalDate entry;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", 
      joinColumns = @JoinColumn(name = "IdUser", referencedColumnName = "Id"), 
      inverseJoinColumns = @JoinColumn(name = "IdRole", referencedColumnName = "Id"))
	private List<Role> roles;
	
	public void hidePass() {
		setPass("******");
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Role> getRoles() {
		return roles;
	}
	
	public String getStringRoles() {
		String stringRoles = "";
		for (Role r : this.roles)
			stringRoles += r.getLabel() + "\n";
		return stringRoles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	@Override
	public String toString() {
		return "[" + getId() + "] " + getLogin() + " : " + getRoles().toString();
	}

	public LocalDate getEntry() {
		return entry;
	}

	public void setEntry(LocalDate entry) {
		this.entry = entry;
	}

	public Role getHeightRole() {
		
		Role role = null;
		if (roles != null && !roles.isEmpty()) {
			for (Role r : roles) {
				if (role != null && r.getLevel() > role.getLevel())
					role = r;
				else if (role == null)
					role = r;
			}
		}
		
		return role;
		
	}
	
}
