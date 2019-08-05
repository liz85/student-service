package com.example.StudentsService.dao;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.example.StudentsService.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface StudentDao {
	public static final String collectionName = "students";
	public List<Student> getStudents(Optional<Integer> age);
	public String createStudent(Student student) ;
	public Optional<Student> updateStudent(Student student,ObjectId id);

}
