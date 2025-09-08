package com.example.databasebackuputility.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "backup")
public class DatabaseConfig {
    private DbConfig mysql;
    private DbConfig postgresql;
    private DbConfig mongodb;
    private SqliteConfig sqlite;
    private CloudConfig awsS3;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DbConfig {
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SqliteConfig {
        private String filepath;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CloudConfig {
        private String bucket;
        private String accessKey;
        private String secretKey;
        private String region;
    }
}