package models;

import java.time.LocalDate;

public class LibraryRecord {
    private static int nextId = 1;
    private int recordId;
    private String recordType;
    private LocalDate date;
    private String studentId;
    private String status;
    private String librarianId;

    public LibraryRecord(String recordType, String studentId, String status) {
        this.recordId = nextId++;
        this.recordType = recordType;
        this.date = LocalDate.now();
        this.studentId = studentId;
        this.status = status;
        this.librarianId = "";
    }

    public LibraryRecord(int recordId, String recordType, LocalDate date, String studentId, String status, String librarianId) {
        this.recordId = recordId;
        this.recordType = recordType;
        this.date = date;
        this.studentId = studentId;
        this.status = status;
        this.librarianId = (librarianId == null) ? "" : librarianId;
        if (recordId >= nextId) nextId = recordId + 1;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getRecordType() {
        return recordType;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }

    public String getLibrarianId() {
        return librarianId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLibrarianId(String librarianId) {
        this.librarianId = librarianId;
    }

    @Override
    public String toString() {
        return "RecordID: " + recordId + ", Type: " + recordType + ", Date: " + date +
                ", StudentID: " + studentId + ", Status: " + status + ", LibrarianID: " + librarianId;
    }

    public String toPersistString() {
        return recordId + "|" + escape(recordType) + "|" + date.toString() + "|" + escape(studentId) + "|" + escape(status) + "|" + escape(librarianId);
    }

    public static LibraryRecord fromPersistString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        try {
            int id = Integer.parseInt(p[0]);
            String type = unescape(p[1]);
            LocalDate date = LocalDate.parse(p[2]);
            String studentId = unescape(p[3]);
            String status = unescape(p[4]);
            String librarianId = unescape(p[5]);
            return new LibraryRecord(id, type, date, studentId, status, librarianId);
        } catch (Exception e) {
            return null;
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "\\|");
    }

    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\|", "|");
    }
}
