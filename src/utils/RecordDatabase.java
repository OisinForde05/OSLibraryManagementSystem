package utils;

import models.LibraryRecord;
import java.util.*;

public class RecordDatabase {
    private static final List<LibraryRecord> records = Collections.synchronizedList(new ArrayList<>());

    public static synchronized LibraryRecord createRecord(String recordType, String studentId, String status) {
        LibraryRecord record = new LibraryRecord(recordType, studentId, status);
        records.add(record);
        return record;
    }

    public static synchronized List<LibraryRecord> getAllRecords() {
        return new ArrayList<>(records);
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
            return true;
        }
        return false;
    }

    public static synchronized boolean updateStatus(int id, String status) {
        LibraryRecord record = getRecordById(id);
        if (record != null) {
            record.setStatus(status);
            return true;
        }
        return false;
    }
}
