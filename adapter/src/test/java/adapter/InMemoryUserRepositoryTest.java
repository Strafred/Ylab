package adapter;

import model.user.User;
import model.user.UserRole;
import org.junit.jupiter.api.Test;
import ylab.adapter.out.InMemoryUserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryUserRepositoryTest {
    @Test
    void getUser_shouldWork() {
        var inMemoryUserRepository = new InMemoryUserRepository();
        var user = inMemoryUserRepository.getUser("admin");

        assertThat(user).isNotNull();
    }

    @Test
    void userExists_shouldWork() {
        var inMemoryUserRepository = new InMemoryUserRepository();
        var userExists = inMemoryUserRepository.userExists("admin");

        assertThat(userExists).isTrue();
    }

    @Test
    void saveUser_shouldWork() {
        var inMemoryUserRepository = new InMemoryUserRepository();
        var user = new User("test", "test");
        inMemoryUserRepository.saveUser(user);

        assertThat(inMemoryUserRepository.userExists("test")).isTrue();
    }
}
