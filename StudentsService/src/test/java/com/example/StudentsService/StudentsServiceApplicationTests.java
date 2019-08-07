package com.example.StudentsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.StudentsService.exception.StudentNotFoundException;
import com.example.StudentsService.exception.StudentValidationException;
import com.example.StudentsService.model.ErrorDetails;
import com.example.StudentsService.model.Student;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentsServiceApplicationTests {
	
	@Autowired
	StudentServiceController studentServicerviceController;

	
	  @Test 
	  public void testGetAllStudents() 
	  { 
		  String url="http://localhost:8080/students";
	  
		 // RestTemplate restTemplate = new RestTemplate(); List<Student> studentList =
		 // restTemplate.getForObject(url , List.class); List<Student> testList = new
		List<Student> testList = studentServicerviceController.getAllStudents(null, null);
		assertThat(null!=testList&&testList.size()>0);
	  
	  }
	 
	  @Test 
	  public void testGetSomeStudents() 
	  { 
		  String url="http://localhost:8080/students?age=67&grade=a";
	  
		 // RestTemplate restTemplate = new RestTemplate(); List<Student> studentList =
		 // restTemplate.getForObject(url , List.class); List<Student> testList = new
		  List<Student> testList = studentServicerviceController.getAllStudents("67", "a");
			assertThat(null!=testList||testList.size()>0);
	  
	  }
	  
	@Test
	public void testGetAllStudentsNotFound() {
		String url = "http://localhost:8080/students?age=93";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> ent = new HttpEntity<String>(requestHeaders);
		try {
			//studentServicerviceController = new StudentServiceController();
			//RestTemplate restTemplate = new RestTemplate();
			//ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, ent, String.class);
			List<Student> testList = studentServicerviceController.getAllStudents("44", "D");
		} catch (Exception e) {
			assertThat(e).isInstanceOf(StudentNotFoundException.class);

		}

	}

	@Test
	public void testCreateStudent() {
		String url = "http://localhost:8080/students";
		Student testStudent = new Student("George1", 67, 90);
	//	RestTemplate restTemplate = new RestTemplate();
		//URI location = restTemplate.postForLocation(url, testStudent);
		ResponseEntity<URI> location = studentServicerviceController.createStudent(testStudent);
		assertNotNull(location);

	}
	@Test
	public void testCreateInvalidStudent() {
		String url = "http://localhost:8080/students";
		Student testStudent = new Student("George1", 167, 90);
	//	RestTemplate restTemplate = new RestTemplate();
		//URI location = restTemplate.postForLocation(url, testStudent);
		try {
			//ResponseEntity<ErrorDetails> response = restTemplate.exchange(url, HttpMethod.POST, ent, ErrorDetails.class);
				ResponseEntity<URI> response = studentServicerviceController.createStudent(testStudent);
			}
		catch(Exception e) {
				assertThat(e).isInstanceOf(ConstraintViolationException.class);
			}

	}

	@Test
	public void testInvalidCreateStudent() {
		String url = "http://localhost:8080/students";
		Student testStudent = new Student();
		testStudent.setAge(12);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Student> ent = new HttpEntity<Student>(testStudent, requestHeaders);
		//RestTemplate restTemplate = new RestTemplate();
		try {
		//ResponseEntity<ErrorDetails> response = restTemplate.exchange(url, HttpMethod.POST, ent, ErrorDetails.class);
			ResponseEntity<URI> response = studentServicerviceController.createStudent(testStudent);
		}catch(Exception e) {
			assertThat(e).isInstanceOf(ConstraintViolationException.class);
		}

	}

	@Test
	public void testUpdateStudent() {
		String url = "http://localhost:8080/students/5d3ecaf311b4ff7d233762ad";
		Map<String, String> inputMap = new HashMap<String, String>();
		Student testStudent = new Student("George5", 67, 35);
		HttpEntity<Student> request = new HttpEntity<>(testStudent);
		//RestTemplate restTemplate = new RestTemplate();
		//ResponseEntity<Student> en = restTemplate.exchange(url, HttpMethod.PUT, request, Student.class);
		ResponseEntity<Student> en = studentServicerviceController.updateStudent(testStudent, "5d3ecaf311b4ff7d233762ad");
		System.out.println(en.getBody());
		assertEquals(en.getStatusCode(), HttpStatus.OK);

	}
	
	@Test
	public void testInvalidUpdateStudent() {
		String url = "http://localhost:8080/students/5d3ecaf311b4ff7d233762";
		Map<String, String> inputMap = new HashMap<String, String>();
		Student testStudent = new Student("George5", 67, 90);
		HttpEntity<Student> request = new HttpEntity<>(testStudent);
		try {
		//RestTemplate restTemplate = new RestTemplate();
		//ResponseEntity<Student> en = restTemplate.exchange(url, HttpMethod.PUT, request, Student.class);
		ResponseEntity<Student> en = studentServicerviceController.updateStudent(testStudent, "5d3ecaf311b4ff7d233762");
			
		}catch(Exception e) {
			assertThat(e).isInstanceOf(StudentValidationException.class);
		}

	}
	@Test
	public void testNotFoundUpdateStudent() {
		String url = "http://localhost:8080/students/5d3ecaf311b4ff7d23376265";
		Map<String, String> inputMap = new HashMap<String, String>();
		Student testStudent = new Student("George5", 67, 90);
		HttpEntity<Student> request = new HttpEntity<>(testStudent);
		try {
		//RestTemplate restTemplate = new RestTemplate();
		//ResponseEntity<Student> en = restTemplate.exchange(url, HttpMethod.PUT, request, Student.class);
		ResponseEntity<Student> en = studentServicerviceController.updateStudent(testStudent, "5d3ecaf311b4ff7d23376265");
			
		}catch(Exception e) {
			assertThat(e).isInstanceOf(StudentNotFoundException.class);
		}

	}

}
