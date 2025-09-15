package com.example.databasebackuputility.service;

import com.example.databasebackuputility.config.DatabaseConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.nio.file.Path;

@Service
public class CloudStorageService {
    private DatabaseConfig.CloudConfig cloudConfig;
    private S3Client s3Client;

    public CloudStorageService (DatabaseConfig databaseConfig) {
        this.cloudConfig = databaseConfig.getAwsS3 ();
        this.s3Client = S3Client.builder ()
                .region (Region.of (this.cloudConfig.getRegion ()))
                .credentialsProvider (StaticCredentialsProvider.create (AwsBasicCredentials.create (cloudConfig.getAccessKey (), cloudConfig.getSecretKey ())))
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