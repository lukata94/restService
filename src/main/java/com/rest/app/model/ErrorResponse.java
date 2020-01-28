package com.rest.app.model;

public class ErrorResponse implements IResponse {
	private String message;

	public ErrorResponse(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
