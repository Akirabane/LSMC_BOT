package Services;

import Entities.User;
import Repositories.UserRepository;
import Repositories.UserRepositoryImpl;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public void addUser(long user_id, String username) {
        userRepository.saveUser(new User(user_id, username));
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUserByUserId(long user_id) {
        return userRepository.getUserByUserId(user_id);
    }

    public void updateNameOfEmployee(long user_id, String username) {
        userRepository.updateNameOfEmployee(new User(user_id, username));
    }

    public void updateRankOfEmployee(String grade_rp, long user_id) {
        userRepository.updateRankOfEmployee(new User(grade_rp, user_id));
    }

    public void deleteUser(long user_id) {
        userRepository.deleteUser(user_id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public List<String> getPermissionsByRole(String role) {
        return userRepository.getPermissionsByRole(role);
    }
}
