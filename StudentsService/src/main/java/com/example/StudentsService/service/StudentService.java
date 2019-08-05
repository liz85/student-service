package com.example.StudentsService.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.example.StudentsService.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StudentService {
	public List<Student> getStudents(Optional<Integer> age,String grade);
	public String createStudent(Student student) ;
	public Optional<Student> updateStudent(Student student,ObjectId id);

}
