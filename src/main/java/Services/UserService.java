package Services;

import Entities.User;
import Repositories.UserRepository;
import Repositories.UserRepositoryImpl;

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

    public User getUserByUserId(long id) {
        return userRepository.getUserByUserId(id);
    }

    public void updateUser(int id, String username) {
        User user = new User(id, username);
        userRepository.updateUser(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    public void getAllUsers() {
        userRepository.getAllUsers();
    }
}
