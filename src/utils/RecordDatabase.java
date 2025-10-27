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
}
