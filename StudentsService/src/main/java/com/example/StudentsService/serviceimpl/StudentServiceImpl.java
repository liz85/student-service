package com.example.StudentsService.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.StudentsService.dao.StudentDao;
import com.example.StudentsService.model.Student;
import com.example.StudentsService.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class StudentServiceImpl implements StudentService{
	@Autowired
	StudentDao studentDao;
	
	public List<Student> getStudents(Optional<Integer> age,String gradeVal){
		List<Student> studentList = studentDao.getStudents(age);
		List<Student> studentListFiltered;
		
		studentList.stream().forEach(student->setGrade(student));
		if(null!=gradeVal && null!=studentList && studentList.size()>0) {
			
			studentListFiltered = studentList.stream().filter(student->student.getGrade().equalsIgnoreCase(gradeVal)).collect(Collectors.toList());
			return studentListFiltered;
		}

		return studentList;
	}
	public String createStudent(Student student)  {
		String id =studentDao.createStudent(student);
		return id;
	}
	public Optional<Student> updateStudent(Student student,ObjectId id) {
		 Optional<Student> stu =studentDao.updateStudent(student, id);
		 stu.ifPresent(s->setGrade(s));
		 return stu;
	}
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
