package com.pwc.utilities.tron.model.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;





@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

	@Column(name = "name")
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	@JsonIgnoreProperties("roles")
    private Set<LocalUser> userRoles;
	
	
	@ManyToMany
	@JsonIgnoreProperties("roles")
    @JoinTable(
        name = "roles_privileges", 
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<LocalUser> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<LocalUser> userRoles) {
		this.userRoles = userRoles;
	}

	public Collection<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	
}
