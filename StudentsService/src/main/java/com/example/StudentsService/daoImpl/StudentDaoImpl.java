package com.example.StudentsService.daoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
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

@Service
public class StudentDaoImpl implements StudentDao{

	@Autowired
	MongoDatabase mongoDatabase;
	
	public List<Student> getStudents(Optional<Integer> age) {
		List<Student> studentList = new ArrayList<Student>();
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		
		FindIterable<Document> iterDoc; 
		if(age.isPresent()) {
			
			iterDoc = collection.find(Filters.eq("age",age.get()));
			
		}else {
			iterDoc = collection.find();
		}
		studentList = iterDoc.map( this::mapStudent).into(new ArrayList<Student>());
		System.out.println("StudentList:"+studentList);
		return studentList;
	} 
	public String createStudent(Student student) 
	{
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		
		Document doc = mapToDoc(student);
		collection.insertOne(doc);
		ObjectId id = null!=doc?(ObjectId)doc.get( "_id" ):null;
		String idString =null!=id?id.toString():null;
		return idString;
	}
	
	public Optional<Student> updateStudent(Student student,ObjectId id) {
		Optional<Student> studentOp =Optional.empty();// = Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("name").is("Liz")), Student.class));
		MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
		Document doc = mapToDoc(student);
		Document docUpdate =collection.findOneAndReplace(Filters.eq("_id", id),doc,new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER));
		if(null!=docUpdate)
		studentOp = Optional.ofNullable(mapStudent(docUpdate));
		return studentOp;
	}
	public Student mapStudent(Document doc) {
		Student student = new Student();
		student.setId(doc.getObjectId("_id").toString());
		student.setName(doc.getString("name"));
		student.setAge(doc.getInteger("age"));
		student.setMarks(doc.getInteger("marks"));
		return student;
	}
	public Document mapToDoc(Student student) {
		Document doc =new Document();
		if(null!=student && student.getName()!=null)doc.append("name", student.getName());
		if(null!=student && student.getAge()!=null)doc.append("age", student.getAge());
		if(null!=student && student.getMarks()!=null)doc.append("marks", student.getMarks());
		return doc;
	}
}
