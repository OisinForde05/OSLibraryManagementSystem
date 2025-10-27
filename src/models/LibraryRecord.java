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
}
