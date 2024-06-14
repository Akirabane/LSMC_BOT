package Entities;

import java.util.List;

public class User {
    private int id;
    private String username;
    private long user_id;
    private String grade_rp;
    private String role;
    private List<String> permissions;

    public User(long user_id, String username, String grade_rp, String role, List<String> permissions) {
        this.user_id = user_id;
        this.username = username;
        this.grade_rp = grade_rp;
        this.role = role;
        this.permissions = permissions;
    }

    public User(long user_id, String username, String grade_rp, String role) {
        this.user_id = user_id;
        this.username = username;
        this.grade_rp = grade_rp;
        this.role = role;
    }

    public User(long user_id, String username, String grade_rp) {
        this.user_id = user_id;
        this.username = username;
        this.grade_rp = grade_rp;
    }

    public User(long user_id, String username) {
        this.user_id = user_id;
        this.username = username;
    }

    public User(String grade_rp, long user_id) {
        this.grade_rp = grade_rp;
        this.user_id = user_id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getUserId() {
        return user_id;
    }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGradeRp() {
        return grade_rp;
    }

    public void setGradeRp(String grade_rp) {
        this.grade_rp = grade_rp;
    }
}
