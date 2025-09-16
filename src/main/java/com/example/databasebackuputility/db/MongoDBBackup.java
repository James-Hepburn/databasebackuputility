package com.example.databasebackuputility.db;

import com.example.databasebackuputility.config.DatabaseConfig;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class MongoDBBackup implements DatabaseBackup{
    private final DatabaseConfig.DbConfig config;

    @Override
    public File backup (String backupDir) throws Exception {
        File outDir = new File (backupDir, this.config.getDatabase () + "_mongo");
        String command = String.format ("mongodump --host %s --port %d --username %s --password %s --db %s --out %s",
                this.config.getHost (), this.config.getPort (), this.config.getUsername (), this.config.getPassword (),
                this.config.getDatabase (), outDir.getAbsolutePath ());
        Process process = Runtime.getRuntime ().exec (command);

        if (process.waitFor () != 0) {
            throw new RuntimeException ("mongodump failed");
        }

        return outDir;
    }

    @Override
    public void restore (File backupFile) throws Exception {
        String command = String.format ("mongorestore --host %s --port %d --username %s --password %s --db %s %s",
                this.config.getHost (), this.config.getPort (), this.config.getUsername (), this.config.getPassword (),
                this.config.getDatabase (), backupFile.getAbsolutePath ());
        Process process = Runtime.getRuntime ().exec (command);

        if (process.waitFor () != 0) {
            throw new RuntimeException ("mongorestore failed");
        }
    }
}