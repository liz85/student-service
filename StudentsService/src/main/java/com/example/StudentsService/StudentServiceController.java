package com.example.StudentsService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.StudentsService.exception.StudentNotFoundException;
import com.example.StudentsService.exception.StudentValidationException;
import com.example.StudentsService.model.Student;
import com.example.StudentsService.service.StudentService;

/**
 * @author 807157
 * The class exposes Restful services for creating, updating, searching student details
 * 
 *
 */
@Validated
@RestController
@RequestMapping(path="/students")
public class StudentServiceController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	StudentService studentService;
	
	
	/**
	 * @param age
	 * @param grade
	 * @return List<Student>
	 * The method is for GET service for searching student details,
	 * if no parameters(not mandatory) are sent in request, all students are fetched.
	 * http://localhost:8080/students?age=16@grade=A or http://localhost:8080/students
	 * Validations for age and grade(should be in A,B,C,D,E)
	 */
	@GetMapping
	public List<Student> getAllStudents(@RequestParam(required = false)@Min(value = 2,message = "'age' must be atleaset 2") @Max(
            value = 100,message = "'age' must not be greater than 100")String age,@RequestParam(required = false)
	@Pattern(regexp = "A|B|C|D|E", flags = Pattern.Flag.CASE_INSENSITIVE ,message="'grade' must be A,B,C,D,E")String grade){
		logger.info("Calling Get Student Service");
		List<Student> studentList = new ArrayList<Student>();
		Optional<Integer> ageParam =Optional.empty();
		Optional<String> ageVal =Optional.ofNullable(age);
		if(ageVal.isPresent()) {
			ageParam = Optional.ofNullable(Integer.parseInt(ageVal.get()));
		}
		logger.info("Calling Get Student Service class method");
		studentList = studentService.getStudents(ageParam,grade);
		if(null==studentList ||studentList.size()==0)
			throw new StudentNotFoundException("No students are found.");
		logger.info("StudentList:"+studentList);
		return studentList;
		
	}
	/**
	 * The method is for POST service for adding a student
	 * @param student for getting input
	 * The Student input is validated using validation framework
	 * Sample:{"name":"GeorgeSeb","age":"83","marks":50}
	 */
	@PostMapping
	public ResponseEntity<URI>  createStudent(@RequestBody @Valid Student student){
				String id= studentService.createStudent(student);
		logger.info("Calling Create Student Service");
		//Building uri for the new student :not implemented
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
				"/{id}").buildAndExpand(id).toUri();

		ResponseEntity<URI> res =ResponseEntity.created(location).build();
		logger.info("Created Student: "+location);
		return res;
		
		
	}
	/**
	 * The method is for PUT service for updating a student
	 * @param student for getting input 
	 * @param id for getting the id for student to be updated.
	 * The Student input is validated using validation framework
	 * Sample:{"name":"GeorgeSeb","age":"83","marks":50}
	 */	
	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student,@PathVariable(required=true)String id) throws StudentNotFoundException{
		try {
		logger.info("Calling Update Student Service for Id:"+id);
		ObjectId idObj = new ObjectId(id);
		Optional<Student> studentUpdate = studentService.updateStudent(student,idObj);
		if(studentUpdate.isPresent()) {
			return new ResponseEntity<Student>(studentUpdate.get(), HttpStatus.OK);
		}else{
			//When no student with the given id is present, StudentNotFoundException is thrown
			logger.warn("The student not found with id:"+id);
			throw new StudentNotFoundException("Student not found.");
		}
		}catch (IllegalArgumentException e) {
			//When id in request is not a valid ObjectId
			logger.warn("The given Id is invalid:"+id);
			throw new StudentValidationException("The given Id is invalid");
			
		}
		
	}
}
