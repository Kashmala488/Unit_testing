package User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
 private List<User> users = new ArrayList<>();

 public User findById(Long id) {
     return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
 }

 public void save(User user) {
     users.add(user);
 }

 public boolean delete(Long id) {
     User user = findById(id);
     if (user != null) {
         users.remove(user);
         return true;
     }
     return false;
 }

 public List<User> findAll() {
     return new ArrayList<>(users);
 }

 public boolean existsByUsername(String username) {
     return users.stream().anyMatch(user -> user.getUsername().equals(username));
 }
}

