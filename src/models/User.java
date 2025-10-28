package models;

public class User {
    private String name;
    private String studentId;
    private String email;
    private String password;
    private String department;
    private String role;

    public User(String name, String studentId, String email, String password, String department, String role) {
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.password = password;
        this.department = department;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return name + "," + studentId + "," + email + "," + password + "," + department + "," + role;
    }

    public static User fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length != 6) return null;
        return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}
