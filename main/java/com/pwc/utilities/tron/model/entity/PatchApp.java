package com.pwc.utilities.tron.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PATCH_APP")
public class PatchApp extends BaseEntity {

	
	
	@ManyToOne()
	@JoinColumn(name = "patch_id")
	private Patch patch;
	
	@Column(name ="file")
	private String file;
	
	@Column(name = "path")
	private String path;


	public Patch getPatch() {
		return patch;
	}

	public void setPatch(Patch patch) {
		this.patch = patch;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	

}
