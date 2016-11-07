package com.pwc.utilities.tron.model.entity;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;


@Entity
@Table(name = "PATCH")
public class Patch extends BaseEntity {

	@ManyToOne
    @JoinColumn(name = "env_id")
	private Environment env;
	
	@Column(name = "description")
	private String desc;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "isAppIncl")
	private String isAppIncl;
	
	@Column(name = "isDbIncl")
	private String isDbIncl;
	
	@Column(name = "isBundleIncl")
	private String isBundleIncl;
	
	@Column(name = "isPreReqIncl")
	private String isPreReqIncl;
	
	@Column(name = "destLoc")
	private String dest;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "moduleNbr")
	private String moduleNbr;
	
	@Column(name = "version")
	private String version;
	

	@Column(name = "createdBy")
	private String createdBy;
	
	@Column(name = "createDttm")
	private Date createDttm;
	
	@Column(name = "updateDttm")
	private Date updateDttm;
	
	@OneToMany(mappedBy = "patch", cascade = CascadeType.ALL)
	protected Set<PatchDb> patchDbs;

	@OneToMany(mappedBy = "patch", cascade = CascadeType.ALL)
	protected Set<PatchApp> patchApp;
	
	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsAppIncl() {
		return isAppIncl;
	}

	public void setIsAppIncl(String isAppIncl) {
		this.isAppIncl = isAppIncl;
	}

	public String getIsDbIncl() {
		return isDbIncl;
	}

	public void setIsDbIncl(String isDbIncl) {
		this.isDbIncl = isDbIncl;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDttm() {
		return createDttm;
	}

	public void setCreateDttm(Date createDttm) {
		this.createDttm = createDttm;
	}

	public Date getUpdateDttm() {
		return updateDttm;
	}

	public void setUpdateDttm(Date updateDttm) {
		this.updateDttm = updateDttm;
	}


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsBundleIncl() {
		return isBundleIncl;
	}

	public void setIsBundleIncl(String isBundleIncl) {
		this.isBundleIncl = isBundleIncl;
	}

	public String getIsPreReqIncl() {
		return isPreReqIncl;
	}

	public void setIsPreReqIncl(String isPreReqIncl) {
		this.isPreReqIncl = isPreReqIncl;
	}

	public String getModuleNbr() {
		return moduleNbr;
	}

	public void setModuleNbr(String moduleNbr) {
		this.moduleNbr = moduleNbr;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	

}
