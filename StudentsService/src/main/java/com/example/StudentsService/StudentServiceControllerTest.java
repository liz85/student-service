package com.example.StudentsService;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Response;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.StudentsService.model.ErrorDetails;
import com.example.StudentsService.model.Student;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StudentServiceControllerTest {
	
	
	
	@Test
	public void testGetAllStudents() {
		String url="http://localhost:8080/students?age=67&grade=a";
		
	      RestTemplate restTemplate = new RestTemplate();
	      List<Student> studentList = restTemplate.getForObject(url , List.class);
	     List<Student> testList = new ArrayList<Student> ();
	     testList.add(new Student("5d3ecaf311b4ff7d233762ad","George1",67,90,"A"));
	     assertArrayEquals(testList.toArray(), studentList.toArray());
	  
	}
	@Test
	public void testGetAllStudentsNotFoung() {
		String url="http://localhost:8080/students?age=12";
		Map <String,String> inputMap =new HashMap<String,String>();
		 HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity ent = new HttpEntity(requestHeaders);
	      RestTemplate restTemplate = new RestTemplate();
	      ResponseEntity<String>  response= restTemplate.exchange(url ,HttpMethod.GET, ent,String.class);
	     
	  
	}
	@Test
	public void testCreateStudent() {
		String url="http://localhost:8080/students";
		Student testStudent = new Student("George1",67,90);
	      RestTemplate restTemplate = new RestTemplate();
	     URI location = restTemplate.postForLocation(url ,testStudent);
	     assertNotNull(location);
	  
	}
	@Test
	public void testUpdateStudent() {
		String url="http://localhost:8080/students/{id}";
		Map <String,String> inputMap =new HashMap<String,String>();
		Student testStudent = new Student("George5",67,90);
		HttpEntity<Student> request = new HttpEntity<>(testStudent);
	      RestTemplate restTemplate = new RestTemplate();
	      ResponseEntity<Student> en =restTemplate.exchange(url ,HttpMethod.PUT,request,Student.class);
	     System.out.println(en.getBody());
	     assertEquals(en.getStatusCode(), HttpStatus.OK.value());
	    // assertNotNull(location);
	  
	}

}
