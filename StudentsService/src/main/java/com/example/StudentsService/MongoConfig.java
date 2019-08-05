package com.example.StudentsService;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;


@Configuration
public class MongoConfig {
	
	private static final String MONGO_DB_URL = "localhost:27017";
    private static final String MONGO_DB_NAME = "studentservice";
    
    
    @Bean
    public MongoDatabase mongoDatabase() throws IOException {
       // EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
      //  mongo.setBindIp(MONGO_DB_URL);
        MongoClient mongoClient =new MongoClient("localhost",27017);
        MongoDatabase database = mongoClient.getDatabase(MONGO_DB_NAME);
        return database;
    }

}
