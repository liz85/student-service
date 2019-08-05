package com.example.StudentsService.model;

import java.time.LocalDateTime;
import java.util.Date;

public class ErrorDetails {
	private String exTime;
	private String cause;
	private String message;
	public String getExTime() {
		return exTime;
	}
	public void setExTime(String exTime) {
		this.exTime = exTime;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorDetails(String exTime, String cause, String message) {
		super();
		this.exTime = exTime;
		this.cause = cause;
		this.message = message;
	}
	public ErrorDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
