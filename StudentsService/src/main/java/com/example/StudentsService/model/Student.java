package com.example.StudentsService.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class Student {
	
	
	private String id;
	
	@NotNull(message="'name' cannot be null")
	@Size(min = 3, message 
    = "'name' must have atleast 3 characters")
	private String name;
	
	@NotNull(message="'age' cannot be null")
	@Min(
            value = 2,
            message = "'age' must be atleaset 2"
    )
	@Max(
            value = 100,
            message = "'age' must not be greater than 100"
    )
	private Integer age;
	
	@NotNull(message="'marks' cannot be null")
	@Min(
            value = 2,
            message = "'marks' must be atleaset 2"
    )
	@Max(
            value = 100,
            message = "'marks' must not be greater than 100"
    )
	private Integer marks;
	
	private String grade;
	

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getMarks() {
		return marks;
	}
	public void setMarks(Integer marks) {
		this.marks = marks;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Student()
	{
		super();
	}
	
	public Student( String name, Integer age, Integer marks) {
		super();
		this.name = name;
		this.age = age;
		this.marks = marks;
	}

	
	
}
