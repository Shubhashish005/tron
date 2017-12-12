package com.pwc.utilities.tron.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PREREQ")
public class Prerequisite extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "patch_id")
	private Patch patch;
	
	@Column(name = "package_name")
	private String packageName;

	public Patch getPatch() {
		return patch;
	}

	public void setPatch(Patch patch) {
		this.patch = patch;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
	
}
