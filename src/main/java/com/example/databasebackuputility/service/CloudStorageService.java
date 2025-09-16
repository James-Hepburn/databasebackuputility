package com.example.databasebackuputility.service;

import com.example.databasebackuputility.config.DatabaseConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;

@Service
public class CloudStorageService {
    private final DatabaseConfig databaseConfig;
    private DatabaseConfig.CloudConfig cloudConfig;
    private S3Client s3Client;

    public CloudStorageService (DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @PostConstruct
    public void init () {
        this.cloudConfig = databaseConfig.getAwsS3 ();

        if (this.cloudConfig == null) {
            throw new IllegalStateException ("AWS S3 config is missing!");
        }

        this.s3Client = S3Client.builder ()
                .region (Region.of (this.cloudConfig.getRegion ()))
                .credentialsProvider (StaticCredentialsProvider.create (AwsBasicCredentials.create (this.cloudConfig.getAccessKey (), this.cloudConfig.getSecretKey ())))
                .build ();
    }

    public String upload (File file) {
        String key = file.getName ();
        PutObjectRequest request = PutObjectRequest.builder ().bucket (this.cloudConfig.getBucket ()).key (key).build ();
        this.s3Client.putObject (request, RequestBody.fromFile (file));
        return key;
    }

    public File download (String key, String destinationDir) {
        File outFile = new File (destinationDir, key);
        GetObjectRequest request = GetObjectRequest.builder ().bucket (this.cloudConfig.getBucket ()).key (key).build ();
        this.s3Client.getObject (request, outFile.toPath ());
        return outFile;
    }

    public void delete (String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder ().bucket (this.cloudConfig.getBucket ()).key (key).build ();
        this.s3Client.deleteObject (request);
    }
}