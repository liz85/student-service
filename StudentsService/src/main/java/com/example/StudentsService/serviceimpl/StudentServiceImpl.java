package com.example.StudentsService.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.StudentsService.dao.StudentDao;
import com.example.StudentsService.model.Student;
import com.example.StudentsService.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author 807157
 * The class is the implementation of service layer for creating, updating, searching student details
 * 
 *
 */
@Service
public class StudentServiceImpl implements StudentService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	StudentDao studentDao;
	/*
	 * @param age
	 * @param gradeVal
	 * @return List<Student>
	 * The method is for  searching student details
	 * 
	 */
	public List<Student> getStudents(Optional<Integer> age,String gradeVal){
		logger.info("Calling getStudents()");
		//Get the student list
		List<Student> studentList = studentDao.getStudents(age);
		List<Student> studentListFiltered;
		//Sets the grade for each student based on marks.
		studentList.stream().forEach(student->setGrade(student));
		logger.info(" Get Student Service");
		if(null!=gradeVal && null!=studentList && studentList.size()>0) {
			//If grade is present in the request url, the list is filtered on the basis of grade value
			studentListFiltered = studentList.stream().filter(student->student.getGrade().equalsIgnoreCase(gradeVal)).collect(Collectors.toList());
			return studentListFiltered;
		}

		return studentList;
	}
	
	/*
	 * @param Student student
	 * @return String
	 * The method is for creating student 
	 * 
	 */
	public String createStudent(Student student)  {
		logger.info("Calling createStudent()");
		String id =studentDao.createStudent(student);
		return id;
	}
	
	/*
	 * @param Student student
	 * @return String
	 * The method is for updating student 
	 * 
	 */
	public Optional<Student> updateStudent(Student student,ObjectId id) {
		logger.info("Calling updateStudent()");
		 Optional<Student> stu =studentDao.updateStudent(student, id);
		 stu.ifPresent(s->setGrade(s));
		 return stu;
	}
	
	/*
	 * @param Student student
	 * @return void
	 * The method is for setting grade on the basis of marks
	 * 
	 */
	private void setGrade(Student student) {
		if(student.getMarks()!=null) {
			if(student.getMarks()>=80) {
				student.setGrade("A");
			}else if(student.getMarks()>=70) {
				student.setGrade("B");
	
			}else if(student.getMarks()>=60) {
				student.setGrade("C");
	
			}else if(student.getMarks()>=50) {
				student.setGrade("D");
	
			}else  {
				student.setGrade("E");
	
			}
		}
	}


}
