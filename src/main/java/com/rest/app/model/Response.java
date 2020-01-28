package com.rest.app.model;

public class Response implements IResponse {
	private String id;
	private String login;
	private String name;
	private String type;
	private String avatarUrl;
	private String createdAt;
	private String calculations;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(final String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCalculations() {
		return calculations;
	}

	public void setCalculations(final String calculations) {
		this.calculations = calculations;
	}
}
