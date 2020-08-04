package com.whosup.common;

public class AppErrorMessage {

	private String status;
	private String Message;

	public AppErrorMessage() {
		
	}

	public AppErrorMessage(String status, String message) {
		super();
		this.status = status;
		Message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
