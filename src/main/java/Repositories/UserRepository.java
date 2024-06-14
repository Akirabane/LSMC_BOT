package Repositories;
import Entities.User;

import java.util.List;

public interface UserRepository {
    void saveUser(User user);
    User getUserById(int id);
    User getUserByUserId(long user_id);
    List<User> getAllUsers();
    void updateNameOfEmployee(User user);
    void updateRankOfEmployee(User user);
    void deleteUser(long user_id);
    List<String> getPermissionsByRole(String role);
}