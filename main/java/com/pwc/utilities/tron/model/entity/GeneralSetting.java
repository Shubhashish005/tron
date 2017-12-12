package com.pwc.utilities.tron.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GENERAL_SETTINGS")
public class GeneralSetting extends BaseEntity {

	@Column(name = "name")
	private String name;
	
	@Column(name = "property")
	private String property;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	
}
