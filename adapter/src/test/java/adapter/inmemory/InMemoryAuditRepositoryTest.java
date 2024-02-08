package adapter.inmemory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ylab.adapter.repository.inmemory.InMemoryAuditRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryAuditRepositoryTest {
    @DisplayName("Given username and message, when saveAudit, then add audit to repository")
    @Test
    void givenUsernameAndMessage_saveAudit_shouldAddAuditToRepository() {
        String username = "username";
        String message = "message";
        InMemoryAuditRepository inMemoryAuditRepository = new InMemoryAuditRepository();

        inMemoryAuditRepository.saveAudit(username, message);

        assertThat(inMemoryAuditRepository.getAudit().size()).isEqualTo(1);
        assertThat(inMemoryAuditRepository.getAudit().get(0)).isEqualTo(username + ": " + message);
    }
}
