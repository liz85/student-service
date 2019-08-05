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
	
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	StudentService studentService;
	
	
	/**
	 * @param age
	 * @param grade
	 * @return
	 * The method is for GET service for searching student details
	 * students?age=16@grade=A
	 * Validations for age and grade(should be in A,B,C,D,E)
	 */
	@GetMapping
	public List<Student> getAllStudents(@RequestParam(required = false)@Min(value = 2,message = "'age' must be atleaset 2") @Max(
            value = 100,message = "'age' must not be greater than 100")String age,@RequestParam(required = false)
	@Pattern(regexp = "A|B|C|D|E", flags = Pattern.Flag.CASE_INSENSITIVE ,message="'grade' must be A,B,C,D,E")String grade){
		
		List<Student> studentList = new ArrayList<Student>();
		Optional<Integer> ageParam =Optional.empty();
		Optional<String> ageVal =Optional.ofNullable(age);
		if(ageVal.isPresent()) {
			ageParam = Optional.ofNullable(Integer.parseInt(ageVal.get()));
		}
		studentList = studentService.getStudents(ageParam,grade);
		if(null==studentList ||studentList.size()==0)
			throw new StudentNotFoundException("No students are found.");
		System.out.println("StudentList:"+studentList);
				return studentList;
		
	}
	/**
	 * The method is for POST service for adding a student
	 * @param student for getting input
	 * Sample:{"name":"GeorgeSeb","age":"83","marks":50}
	 */
	@PostMapping
	public ResponseEntity<URI>  createStudent(@RequestBody @Valid Student student){
				String id= studentService.createStudent(student);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
				"/{id}").buildAndExpand(id).toUri();

		ResponseEntity<URI> res =ResponseEntity.created(location).build();
		return res;
		
		
	}
	/**
	 * The method is for PUT service for updating a student
	 * @param student for getting input 
	 * @param id for getting the id for student to be updated.
	 * Sample:{"name":"GeorgeSeb","age":"83","marks":50}
	 */	
	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudent(@RequestBody @Valid Student student,@PathVariable(required=true)String id) throws StudentNotFoundException{
		try {
		ObjectId idObj = new ObjectId(id);
		Optional<Student> studentUpdate = studentService.updateStudent(student,idObj);
		if(studentUpdate.isPresent()) {
			return new ResponseEntity<Student>(studentUpdate.get(), HttpStatus.OK);
		}else{
			throw new StudentNotFoundException("Student not found.");
		}
		}catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			throw new StudentValidationException("The given Id is invalid");
		}
		
	}
}
