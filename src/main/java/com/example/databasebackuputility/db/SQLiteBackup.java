package com.example.databasebackuputility.db;

import com.example.databasebackuputility.config.DatabaseConfig;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
public class SQLiteBackup implements DatabaseBackup{
    private final DatabaseConfig.SqliteConfig config;

    @Override
    public File backup (String backupDir) throws IOException {
        File source = new File (this.config.getFilepath ());
        File destination = new File (backupDir, new File (this.config.getFilepath ()).getName ());
        Files.copy (source.toPath (), destination.toPath (), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        return destination;
    }

    @Override
    public void restore (File backupFile) throws IOException {
        Files.copy (backupFile.toPath (), new File (this.config.getFilepath ()).toPath (), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
}