package adapter;

import org.junit.jupiter.api.Test;
import ylab.adapter.out.InMemoryAuditRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryAuditRepositoryTest {
    @Test
    void givenUsernameAndMessage_saveAudit_shouldAddAuditToRepository() {
        String username = "username";
        String message = "message";
        InMemoryAuditRepository inMemoryAuditRepository = new InMemoryAuditRepository();

        inMemoryAuditRepository.saveAudit(username, message);

        assertThat(inMemoryAuditRepository.getAuditRepository().size()).isEqualTo(1);
        assertThat(inMemoryAuditRepository.getAuditRepository().get(0)).isEqualTo(username + ": " + message);
    }
}
