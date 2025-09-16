package com.example.databasebackuputility.db;

import com.example.databasebackuputility.config.DatabaseConfig;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class PostgreSQLBackup implements DatabaseBackup {
    private final DatabaseConfig.DbConfig config;

    @Override
    public File backup (String backupDir) throws Exception {
        File outFile = new File (backupDir, this.config.getDatabase () + "_pg.sql");
        ProcessBuilder pb = new ProcessBuilder ("pg_dump", "-h", this.config.getHost (),
                "-p", String.valueOf (this.config.getPort ()), "-U", this.config.getUsername(),
                "-F", "c", "-f", outFile.getAbsolutePath (), this.config.getDatabase ());
        pb.environment ().put ("PGPASSWORD", this.config.getPassword ());
        Process process = pb.start ();

        if (process.waitFor () != 0) {
            throw new RuntimeException ("pg_dump failed");
        }

        return outFile;
    }

    @Override
    public void restore (File backupFile) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("pg_restore",
                "-h", this.config.getHost (), "-p", String.valueOf (this.config.getPort ()), "-U", this.config.getUsername (),
                "-d", this.config.getDatabase (), "-c", backupFile.getAbsolutePath ());
        pb.environment ().put ("PGPASSWORD", this.config.getPassword ());
        Process process = pb.start ();

        if (process.waitFor () != 0) {
            throw new RuntimeException ("pg_restore failed");
        }
    }
}