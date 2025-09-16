package com.example.databasebackuputility.service;

import com.example.databasebackuputility.config.DatabaseConfig;
import com.example.databasebackuputility.db.*;
import com.example.databasebackuputility.util.CompressionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@RequiredArgsConstructor
@Service
public class RestoreService {
    private final DatabaseConfig config;

    public void performRestore (String dbType, File backupSource) throws Exception {
        DatabaseBackup handler = createRestoreHandler (dbType);

        if (handler == null) {
            throw new IllegalArgumentException ("Unsupported");
        }

        File actualBackup = backupSource;

        if (CompressionUtil.isCompressed (backupSource)) {
            actualBackup = CompressionUtil.decompress (backupSource);
        }

        handler.restore (actualBackup);
    }

    private DatabaseBackup createRestoreHandler (String dbType) {
        dbType = dbType.toLowerCase ();

        if (dbType.equals ("mysql")) {
            return new MySQLBackup (this.config.getMysql ());
        } else if (dbType.equals ("postgresql")) {
            return new PostgreSQLBackup (this.config.getPostgresql ());
        } else if (dbType.equals ("mongodb")) {
            return new MongoDBBackup (this.config.getMongodb ());
        } else if (dbType.equals ("sqlite")) {
            return new SQLiteBackup (this.config.getSqlite ());
        }

        return null;
    }
}