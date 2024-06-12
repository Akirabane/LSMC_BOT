package Repositories;
import Entities.User;

import java.util.List;

public interface UserRepository {
    void saveUser(User user);
    User getUserById(int id);
    User getUserByUserId(long id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(long user_id);
}