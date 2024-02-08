package adapter.inmemory;

import model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ylab.adapter.repository.inmemory.InMemoryUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InMemoryUserRepositoryTest {
    @DisplayName("When getUser, then return user")
    @Test
    void getUser_shouldWork() {
        var inMemoryUserRepository = new InMemoryUserRepository();
        var user = inMemoryUserRepository.getUser("admin");

        assertThat(user).isNotNull();
    }

    @DisplayName("When userExists, then return true")
    @Test
    void userExists_shouldWork() {
        var inMemoryUserRepository = new InMemoryUserRepository();
        var userExists = inMemoryUserRepository.userExists("admin");

        assertThat(userExists).isTrue();
    }

    @DisplayName("When saveUser, then save user")
    @Test
    void saveUser_shouldWork() {
        var inMemoryUserRepository = new InMemoryUserRepository();
        var user = assertDoesNotThrow(() -> new User("test", "test"));
        inMemoryUserRepository.postUser(user);

        assertThat(inMemoryUserRepository.userExists("test")).isTrue();
    }
}
