package com.example.StudentsService.daoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.StudentsService.dao.StudentDao;
import com.example.StudentsService.exception.StudentNotFoundException;
import com.example.StudentsService.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.lang.Nullable;
import com.mongodb.util.JSON;
/**
 * @author 807157
 * The class is the implementation of dao layer for creating, updating, searching student details
 * 
 *
 */
@Service
public class StudentDaoImpl implements StudentDao{

	@Autowired
	MongoDatabase mongoDatabase;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * @param Optional<Integer> age
	 * @return List<Student>
	 * The method is for creating student 
	 * 
	 */
	public List<Student> getStudents(Optional<Integer> age) {
		List<Student> studentList = new ArrayList<Student>();
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		
		FindIterable<Document> iterDoc; 
		//If age is present query with age criteria, otherwise fetch all students
		if(age.isPresent()) {
			
			iterDoc = collection.find(Filters.eq("age",age.get()));
			
		}else {
			iterDoc = collection.find();
		}
		// Mapping on each of the document in the result to student object
		studentList = iterDoc.map( this::mapStudent).into(new ArrayList<Student>());
		logger.info("StudentList:"+studentList);
		return studentList;
	} 
	
	/*
	 * @param Student student
	 * @return String which is id of the created student
	 * The method is for creating student 
	 * 
	 */
	public String createStudent(Student student) 
	{
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		
		Document doc = mapToDoc(student);
		collection.insertOne(doc);
		ObjectId id = null!=doc?(ObjectId)doc.get( "_id" ):null;
		String idString =null!=id?id.toString():null;
		logger.info("Student created with id:"+idString);
		return idString;
	}
	
	/*
	 * @param Student student
	 * @param ObjectId id : id of the student to be updated
	 * @return Optional<Student> which is the updated student
	 * The method is for updating student 
	 * 
	 */
	public Optional<Student> updateStudent(Student student,ObjectId id) {
		Optional<Student> studentOp =Optional.empty();
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		Document doc = mapToDoc(student);
		//docUpdate has the updated document, if not document is found with the id, docUpdate will be null
		Document docUpdate =collection.findOneAndReplace(Filters.eq("_id", id),doc,new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER));
		
		//If docUpdate is not null, it is mapped to Student object. 
		if(null!=docUpdate) {
			studentOp = Optional.ofNullable(mapStudent(docUpdate));
			logger.info("Student updated"+studentOp);
		}
		return studentOp;
	}
	/*
	 * @param Document doc
	 * @return Student
	 * The method is for mapping Document to student 
	 * 
	 */
	
	public Student mapStudent(Document doc) {
		Student student = new Student();
		student.setId(doc.getObjectId("_id").toString());
		student.setName(doc.getString("name"));
		student.setAge(doc.getInteger("age"));
		student.setMarks(doc.getInteger("marks"));
		return student;
	}
	
	/*
	 * @param Student 
	 * @return Document doc
	 * The method is for mapping Student to Document 
	 * 
	 */
	public Document mapToDoc(Student student) {
		Document doc =new Document();
		if(null!=student && student.getName()!=null)doc.append("name", student.getName());
		if(null!=student && student.getAge()!=null)doc.append("age", student.getAge());
		if(null!=student && student.getMarks()!=null)doc.append("marks", student.getMarks());
		return doc;
	}
}

