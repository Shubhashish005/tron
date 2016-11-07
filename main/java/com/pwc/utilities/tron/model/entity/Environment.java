package com.pwc.utilities.tron.model.entity;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ENVIRONMENT")
public class Environment extends BaseEntity {


	@Column(name = "product")
	private String product;
	
	@Column(name = "project")
	private String project;
	
	@Column(name = "env_name")
	private String envName;
	
	@Column(name = "env_ver")
	private String envVer;
	
	@Column(name = "db_host")
	private String dbHost;
	
	@Column(name = "db_port")
	private BigDecimal dbPort;
	
	@Column(name = "db_sid")
	private String dbSid;
	
	@Column(name = "db_user")
	private String dbUser;
	
	@Column(name = "db_pswd")
	private String dbPswd;
	
	@Column(name = "env_path")
	private String envPath;

	@Column(name = "path_to_packages")
	private String pathToPackages;
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getEnvVer() {
		return envVer;
	}

	public void setEnvVer(String envVer) {
		this.envVer = envVer;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public BigDecimal getDbPort() {
		return dbPort;
	}

	public void setDbPort(BigDecimal dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbSid() {
		return dbSid;
	}

	public void setDbSid(String dbSid) {
		this.dbSid = dbSid;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPswd() {
		return dbPswd;
	}

	public void setDbPswd(String dbPswd) {
		this.dbPswd = dbPswd;
	}

	public String getEnvPath() {
		return envPath;
	}

	public void setEnvPath(String envPath) {
		this.envPath = envPath;
	}

	public String getPathToPackages() {
		return pathToPackages;
	}

	public void setPathToPackages(String pathToPackages) {
		this.pathToPackages = pathToPackages;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	
	

}
