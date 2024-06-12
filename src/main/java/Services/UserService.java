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

    public void addUser(long userId, String username) {
        User user = new User(userId, username);
        userRepository.saveUser(user);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUserByUserId(long user_id) {
        return userRepository.getUserByUserId(user_id);
    }

    public void updateNameOfUser(long user_id, String username) {
        User user = new User(user_id, username);
        userRepository.updateNameOfUser(user);
    }

    public void updateRankOfUser(long user_id, String username) {
        User user = new User(user_id, username);
        userRepository.updateRankOfUser(user);
    }

    public void deleteUser(long user_id) {
        userRepository.deleteUser(user_id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
