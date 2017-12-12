package com.pwc.utilities.tron.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "NOTIFICATION")
public class Notification extends BaseEntity {

	@Column(name = "message")
	private String message;
	
	@Column(name = "status")
	private String status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
