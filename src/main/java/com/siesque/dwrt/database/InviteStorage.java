package com.siesque.dwrt.database;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class InviteStorage {
    private static final Path STORAGE_DIR;
    private static final Path STORAGE_FILE;

    static {
        String workingDir = System.getProperty("user.dir");
        STORAGE_DIR = Paths.get(workingDir, "dwrt");
        STORAGE_FILE = STORAGE_DIR.resolve("invites.txt");

        try {
            if (!Files.exists(STORAGE_DIR)) {
                Files.createDirectories(STORAGE_DIR);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage directory", e);
        }

        try {
            if (!Files.exists(STORAGE_FILE)) {
                Files.createFile(STORAGE_FILE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize invites storage file", e);
        }
    }

    public static String write(String player, String team) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(STORAGE_FILE, StandardOpenOption.APPEND)) {
            String toWrite = String.format("%s|%s%n", player, team);
            writer.write(toWrite);
            return toWrite;
        }
    }

    public static List<String> read(String key) throws IOException {
        List<String> lines = Files.readAllLines(STORAGE_FILE);
        List<String> keyValues = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(key)) {
                keyValues.add(line);
            }
        }

        return keyValues;
    }

    public static boolean canJoin(String player, String team) throws IOException {
        List<String> invites = read(player);

        for (String invite : invites) {
            String[] parts = invite.split("\\|");
            if (parts[1].equals(team)) {
                return true;
            }
        }

        return false;
    }
}
