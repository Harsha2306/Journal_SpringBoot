package com.harsha.project2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class MongoConfig {
	@Bean
	public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}
}