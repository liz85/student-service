package com.example.StudentsService.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.StudentsService.model.ErrorDetails;
import com.example.StudentsService.exception.StudentValidationException;

/**
 * @author 807157
 * The class is the exception handling class
 * 
 *
 */
@ControllerAdvice
public class ExceptionAdvice  extends ResponseEntityExceptionHandler {
	
	/*
	 * @return ResponseEntity
	 * The method is overriden method of ResponseEntityExceptionHandler, it handles validation errors 
	 * When the validation fails, ErrorDetails object is created based on the validation errors
	 * 
	 */

	  @Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	      HttpHeaders headers, HttpStatus status, WebRequest request) {
		  StringBuilder sb = new StringBuilder();
		  for (ObjectError error : ex.getBindingResult().getAllErrors()) {
				sb.append("[").append(error.getDefaultMessage()).append("] ");
				
				
			}
		 
		 ErrorDetails errorDetails = new ErrorDetails(LocalDate.now().toString()+LocalTime.now().toString(), "Validation Failed",
	    		sb.toString());
	    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	  } 
	  /*
		 * @return ResponseEntity
		 * The method is overriden method of ResponseEntityExceptionHandler
		 * Handles if any http request not supported by the student service is called 
		 * When the validation fails, ErrorDetails object is created 
		 * 
		 */
	  @Override
	  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
				HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		  ErrorDetails errorDetails = new ErrorDetails(LocalDate.now().toString()+LocalTime.now().toString(), "Method is not Allowed",
		    		"Not valid methods for service");
		    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
		}
	  /*
		 * @return ResponseEntity<ErrorDetails>
		 * The method is overriden method of ResponseEntityExceptionHandler
		 * Handles StudentValidationException thrown from Controller class
		 * When the validation fails, ErrorDetails object is created 
		 * 
		 */
	  @ExceptionHandler({StudentValidationException.class})
	  public final ResponseEntity<ErrorDetails> handleUserValidationException(Exception ex, WebRequest request) {
		 ErrorDetails errorDetails = new ErrorDetails(LocalDate.now().toString()+LocalTime.now().toString(), "Invalid Arguments",
	        "Invalid Student for:"+request.getDescription(false));
	    return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
	  }
	  
	  /*
		 * @return ResponseEntity<ErrorDetails>
		 * The method is overriden method of ResponseEntityExceptionHandler
		 * Handles StudentNotFoundException thrown from Controller class
		 * When the validation fails, ErrorDetails object is created 
		 * 
		 */
	  @ExceptionHandler(StudentNotFoundException.class)
	  public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) {
		 ErrorDetails errorDetails = new ErrorDetails(LocalDate.now().toString()+LocalTime.now().toString(), "Student not Found",
	        "Student not found :"+request.getDescription(false));
	    return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
	  }
	  
	  /*
		 * @return ResponseEntity<ErrorDetails>
		 * The method is overriden method of ResponseEntityExceptionHandler
		 * Handles ConstraintViolationException thrown from Controller class
		 * When the validation fails, ErrorDetails object is created 
		 * 
		 */
	 @ExceptionHandler(ConstraintViolationException.class)
	  ResponseEntity<ErrorDetails> handleConstraintViolation(ConstraintViolationException e) {
	      Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
	      Set<String> messages = new HashSet<>(constraintViolations.size());
	      messages.addAll(constraintViolations.stream()
	              .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
	              .collect(Collectors.toList()));
	      String msg = messages
	          .toString();
	      ErrorDetails errorDetails = new ErrorDetails(LocalDate.now().toString()+LocalTime.now().toString(), "Invalid Arguments",
			 msg);
	      return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);

	  }

}



