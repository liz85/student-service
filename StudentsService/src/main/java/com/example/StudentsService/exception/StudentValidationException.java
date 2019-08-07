package com.example.StudentsService.exception;

/**
 * @author 807157
 * The class is for when validation fails for student
 * Thrown when invalid id is passed for PUT request to update a student
 * 
 *
 */
public class StudentValidationException extends RuntimeException{

	public StudentValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentValidationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	
}
