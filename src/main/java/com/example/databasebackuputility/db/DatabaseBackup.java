package com.example.databasebackuputility.db;

import java.io.File;

public interface DatabaseBackup {
    File backup (String backupDir) throws Exception;
    void restore (File backupFile) throws Exception;
}