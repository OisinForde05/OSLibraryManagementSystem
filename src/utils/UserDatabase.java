package utils;

import models.User;
import java.util.*;

public class UserDatabase {
    private static final List<User> users = Collections.synchronizedList(new ArrayList<>());

    public static synchronized boolean register(User user) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail()) || u.getStudentId().equalsIgnoreCase(user.getStudentId())) {
                return false;
            }
        }
        users.add(user);
        return true;
    }

    public static synchronized User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static synchronized List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
