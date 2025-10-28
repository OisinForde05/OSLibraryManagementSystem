package utils;

import models.LibraryRecord;
import java.util.*;
import java.io.*;

public class RecordDatabase {
    private static final List<LibraryRecord> records = Collections.synchronizedList(new ArrayList<>());
    private static final String FILE_PATH = "records.txt";

    public static synchronized LibraryRecord createRecord(String recordType, String studentId, String status) {
        LibraryRecord record = new LibraryRecord(recordType, studentId, status);
        records.add(record);
        saveToFile();
        return record;
    }

    public static synchronized List<LibraryRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    public static synchronized List<LibraryRecord> getRecordsByLibrarian(String librarianId) {
        List<LibraryRecord> list = new ArrayList<>();
        for (LibraryRecord r : records) {
            if (r.getLibrarianId() != null && r.getLibrarianId().equalsIgnoreCase(librarianId)) list.add(r);
        }
        return list;
    }

    public static synchronized LibraryRecord getRecordById(int id) {
        for (LibraryRecord r : records) {
            if (r.getRecordId() == id) return r;
        }
        return null;
    }

    public static synchronized boolean assignRecord(int id, String librarianId) {
        LibraryRecord record = getRecordById(id);
        if (record != null) {
            record.setLibrarianId(librarianId);
            record.setStatus("Requested");
            saveToFile();
            return true;
        }
        return false;
    }

    public static synchronized boolean updateStatus(int id, String status) {
        LibraryRecord record = getRecordById(id);
        if (record != null) {
            record.setStatus(status);
            saveToFile();
            return true;
        }
        return false;
    }

    public static synchronized void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (LibraryRecord r : records) writer.println(r.toPersistString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        List<LibraryRecord> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LibraryRecord r = LibraryRecord.fromPersistString(line);
                if (r != null) loaded.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        records.clear();
        records.addAll(loaded);
    }
}
