package com.pwc.utilities.tron.model.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

	private String name;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties("permissions")
    private Collection<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
    
    
}

