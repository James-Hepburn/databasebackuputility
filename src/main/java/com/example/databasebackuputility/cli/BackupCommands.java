package com.example.databasebackuputility.cli;

import com.example.databasebackuputility.service.BackupService;
import com.example.databasebackuputility.service.CloudStorageService;
import com.example.databasebackuputility.service.RestoreService;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "dbbackup", mixinStandardHelpOptions = true, version = "1.0", description = "Database Backup Utility CLI")
public class BackupCommands implements Callable <Integer>{
    @Spec
    Model.CommandSpec spec;

    @Option(names = {"-d", "--db"}, description = "Database type: mysql|postgresql|mongodb|sqlite", required = true)
    private String dbType;

    @Option(names = {"-b", "--backup-dir"}, description = "Directory to store backups", required = true)
    private String backupDir;

    @Option(names = {"-c", "--compress"}, description = "Compress backup (zip)", defaultValue = "false")
    private boolean compress;

    @Option(names = {"--cloud"}, description = "Upload backup to S3 after completion", defaultValue = "false")
    private boolean cloud;

    private BackupService backupService;
    private RestoreService restoreService;
    private CloudStorageService cloudStorageService;

    public BackupCommands (BackupService backupService, RestoreService restoreService, CloudStorageService cloudStorageService) {
        this.backupService = backupService;
        this.restoreService = restoreService;
        this.cloudStorageService = cloudStorageService;
    }

    @Override
    public Integer call () throws Exception {
        this.spec.commandLine ().usage (System.out);
        return 0;
    }

    @Command(name = "backup", description = "Perform a backup of the specified database")
    public void backup () {
        try {
            File backupFile = this.backupService.performBackup (this.dbType, this.backupDir, this.compress);
            System.out.println ("Backup completed: " + backupFile.getAbsolutePath ());

            if (this.cloud) {
                String key = this.cloudStorageService.upload (backupFile);
                System.out.println ("Backup uploaded to S3 with key: " + key);
            }
        } catch (Exception e) {
            System.err.println ("Backup failed: " + e.getMessage ());
            e.printStackTrace ();
        }
    }

    @Command(name = "restore", description = "Restore a backup from a file")
    @Parameters(index = "0", description = "Backup file path")
    public void restore (String backupFilePath) {
        try {
            File backupFile = new File (backupFilePath);
            this.restoreService.performRestore (this.dbType, backupFile);
            System.out.println ("Restore completed from: " + backupFile.getAbsolutePath ());
        } catch (Exception e) {
            System.err.println ("Restore failed: " + e.getMessage ());
            e.printStackTrace ();
        }
    }

    public static void main (String [] args) {
        var context = org.springframework.boot.SpringApplication.run (com.example.databasebackuputility.DatabasebackuputilityApplication.class, args);
        BackupCommands commands = context.getBean (BackupCommands.class);
        int exitCode = new CommandLine (commands).execute (args);
        System.exit (exitCode);
    }
}