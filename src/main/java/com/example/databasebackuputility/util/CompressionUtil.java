package com.example.databasebackuputility.util;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class CompressionUtil {
    public static File compress (File source) throws IOException {
        File zipFile = new File (source.getParent (), source.getName () + ".zip");

        try (FileOutputStream fos = new FileOutputStream (zipFile);
             ZipOutputStream zos = new ZipOutputStream (fos)) {

            if (source.isDirectory ()) {
                zipDirectory (source, source.getName (), zos);
            } else {
                zipFile (source, zos);
            }
        }

        return zipFile;
    }

    private static void zipDirectory (File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles ()) {
            if (file.isDirectory ()) {
                zipDirectory (file, parentFolder + "/" + file.getName (), zos);
            } else {
                zipFile (file, parentFolder, zos);
            }
        }
    }

    private static void zipFile (File file, ZipOutputStream zos) throws IOException {
        zipFile (file, file.getName (), zos);
    }

    private static void zipFile (File file, String zipEntryName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream (file)) {
            ZipEntry entry = new ZipEntry (zipEntryName);
            zos.putNextEntry (entry);

            byte [] buffer = new byte [4096];
            int len;

            while ((len = fis.read (buffer)) != -1) {
                zos.write (buffer, 0, len);
            }

            zos.closeEntry ();
        }
    }

    public static File decompress (File zipFile) throws IOException {
        File outputDir = new File (zipFile.getParent (), zipFile.getName ().replace (".zip", ""));

        if (!outputDir.exists ()) {
            outputDir.mkdirs ();
        }

        try (ZipInputStream zis = new ZipInputStream (new FileInputStream (zipFile))) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry ()) != null) {
                File outFile = new File (outputDir, entry.getName ());
                if (entry.isDirectory ()) {
                    outFile.mkdirs ();
                } else {
                    outFile.getParentFile().mkdirs ();

                    try (FileOutputStream fos = new FileOutputStream (outFile)) {
                        byte [] buffer = new byte [4096];
                        int len;

                        while ((len = zis.read (buffer)) != -1) {
                            fos.write (buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry ();
            }
        }

        return outputDir;
    }

    public static boolean isCompressed (File file) {
        return file.getName ().toLowerCase ().endsWith (".zip");
    }
}