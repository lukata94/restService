package com.rest.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "User", uniqueConstraints = { @UniqueConstraint(columnNames = "ID"),
		@UniqueConstraint(columnNames = "LOGIN") })
public class User implements Serializable {

	private static final long serialVersionUID = -827884131223493233L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Integer userId;

	@Column(name = "LOGIN", unique = true, nullable = false)
	private String login;

	@Column(name = "REQUEST_COUNT", unique = false, nullable = false)
	private int requestCount;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(final Integer userId) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(final int requestCount) {
		this.requestCount = requestCount;
	}
}
