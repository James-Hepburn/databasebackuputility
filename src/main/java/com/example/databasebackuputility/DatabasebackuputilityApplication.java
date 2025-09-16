package com.example.databasebackuputility;

import com.example.databasebackuputility.config.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class
})
@EnableConfigurationProperties(DatabaseConfig.class)
public class DatabasebackuputilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabasebackuputilityApplication.class, args);
	}

}