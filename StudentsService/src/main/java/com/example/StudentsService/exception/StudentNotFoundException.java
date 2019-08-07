package com.example.StudentsService.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
/**
 * @author 807157
 * The class is for when no students are found using GET service
 * and when no student is found for the given id in the PUT request.
 * 
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException{

	public StudentNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	
	public StudentNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}


}
