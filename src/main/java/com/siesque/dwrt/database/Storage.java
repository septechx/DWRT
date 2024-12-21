package com.siesque.dwrt.database;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Storage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path STORAGE_DIR;
    private final Path STORAGE_FILE;

    static {
        String workingDir = System.getProperty("user.dir");
        STORAGE_DIR = Paths.get(workingDir, "dwrt");

        try {
            if (!Files.exists(STORAGE_DIR)) {
                Files.createDirectories(STORAGE_DIR);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage directory", e);
        }
    }

    public Storage(String fileName) {
        this.STORAGE_FILE = STORAGE_DIR.resolve(fileName);

        try {
            if (!Files.exists(STORAGE_FILE)) {
                Files.createFile(STORAGE_FILE);
                saveData(new ArrayList<>());
            } else {
                if (Files.size(STORAGE_FILE) == 0) {
                    saveData(new ArrayList<>());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage file: " + fileName, e);
        }
    }

    public <TData> void saveData(TData data) {
        try (FileWriter writer = new FileWriter(STORAGE_FILE.toFile())) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + STORAGE_FILE, e);
        }
    }

    public <TData> TData loadData(Type typeOfData) {
        if (!STORAGE_FILE.toFile().exists()) {
            throw new RuntimeException("File not found: " + STORAGE_FILE);
        }

        try (FileReader reader = new FileReader(STORAGE_FILE.toFile())) {
            return GSON.fromJson(reader, typeOfData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file: " + STORAGE_FILE, e);
        }
    }
}
