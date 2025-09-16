package com.example.databasebackuputility.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "backup")
public class DatabaseConfig {
    private DbConfig mysql;
    private DbConfig postgresql;
    private DbConfig mongodb;
    private SqliteConfig sqlite;
    private CloudConfig awsS3;

    public DbConfig getMysql () { return mysql; }
    public DbConfig getPostgresql () { return postgresql; }
    public DbConfig getMongodb () { return mongodb; }
    public SqliteConfig getSqlite () { return sqlite; }
    public CloudConfig getAwsS3 () { return awsS3; }

    public void setMysql (DbConfig mysql) { this.mysql = mysql; }
    public void setPostgresql (DbConfig postgresql) { this.postgresql = postgresql; }
    public void setMongodb (DbConfig mongodb) { this.mongodb = mongodb; }
    public void setSqlite (SqliteConfig sqlite) { this.sqlite = sqlite; }
    public void setAwsS3 (CloudConfig awsS3) { this.awsS3 = awsS3; }

    public static class DbConfig {
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;

        public String getHost () { return host; }
        public int getPort () { return port; }
        public String getDatabase () { return database; }
        public String getUsername () { return username; }
        public String getPassword () { return password; }

        public void setHost (String host) { this.host = host; }
        public void setPort (int port) { this.port = port; }
        public void setDatabase (String database) { this.database = database; }
        public void setUsername (String username) { this.username = username; }
        public void setPassword (String password) { this.password = password; }
    }

    public static class SqliteConfig {
        private String filepath;

        public String getFilepath () { return filepath; }
        public void setFilepath (String filepath) { this.filepath = filepath; }
    }

    public static class CloudConfig {
        private String bucket;
        private String accessKey;
        private String secretKey;
        private String region;

        public String getBucket () { return bucket; }
        public String getAccessKey () { return accessKey; }
        public String getSecretKey () { return secretKey; }
        public String getRegion () { return region; }

        public void setBucket (String bucket) { this.bucket = bucket; }
        public void setAccessKey (String accessKey) { this.accessKey = accessKey; }
        public void setSecretKey (String secretKey) { this.secretKey = secretKey; }
        public void setRegion (String region) { this.region = region; }
    }
}