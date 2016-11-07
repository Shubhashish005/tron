package com.pwc.utilities.tron.model.entity;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "APP_OBJ_TYPE_FLG")
public class AppObjTypeFlg extends BaseEntity {
	/**
	 * 
	 */
	
	@Column(name = "APP_OBJ_TYPE")
	@NotEmpty
	private String appObjType;
	
	@Column(name = "DESCRIPTION")
	@NotEmpty
	private String description;
	
	/*
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APP_OBJ_TYPE_ID")
	private BigDecimal appObjTypeId;
	
	@Column(name = "APP_OBJ_TYPE")
	@NotEmpty
	private String appObjType;
	
	@Column(name = "DESCRIPTION")
	@NotEmpty
	private String description;
	
	@ElementCollection(targetClass = PatchApp.class)
	private Set<PatchApp> patchApps = new HashSet<PatchApp>(0);

	public Set<PatchApp> getPatchApps() {
		return patchApps;
	}

	public void setPatchApps(Set<PatchApp> patchApps) {
		this.patchApps = patchApps;
	}

	public BigDecimal getAppObjTypeId() {
		return appObjTypeId;
	}

	public void setAppObjTypeId(BigDecimal appObjTypeId) {
		this.appObjTypeId = appObjTypeId;
	}
	
	public AppObjTypeFlg() {
	}

	public AppObjTypeFlg(String appObjType, String description) {
		this.appObjType = appObjType;
		this.description = description;
	}

	public String getAppObjType() {
		return this.appObjType;
	}

	public void setAppObjType(String appObjType) {
		this.appObjType = appObjType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	*/
}
