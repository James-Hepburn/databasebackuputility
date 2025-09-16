package com.example.databasebackuputility.db;

import com.example.databasebackuputility.config.DatabaseConfig;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class MySQLBackup implements DatabaseBackup {
    private final DatabaseConfig.DbConfig config;

    @Override
    public File backup (String backupDir) throws IOException, InterruptedException {
        File outFile = new File (backupDir, this.config.getDatabase () + "_mysql.sql");
        String command = String.format ("mysqldump -h %s -P %d -u %s -p%s %s -r %s",
                this.config.getHost (), this.config.getPort (), this.config.getUsername (),
                this.config.getPassword (), this.config.getDatabase (), outFile.getAbsolutePath ());
        Process process = Runtime.getRuntime ().exec (command);

        if (process.waitFor () != 0) {
            throw new IOException ("mysqldump failed");
        }

        return outFile;
    }

    @Override
    public void restore (File backupFile) throws IOException, InterruptedException {
        String command = String.format ("mysql -h %s -P %d -u %s -p%s %s < %s",
                this.config.getHost (), this.config.getPort (), this.config.getUsername (),
                this.config.getPassword (), this.config.getDatabase (), backupFile.getAbsolutePath ());
        Process process = Runtime.getRuntime ().exec (new String []{"/bin/sh","-c", command});

        if (process.waitFor () != 0) {
            throw new IOException ("mysql restore failed");
        }
    }
}