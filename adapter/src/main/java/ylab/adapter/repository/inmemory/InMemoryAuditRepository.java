package ylab.adapter.repository.inmemory;

import application.port.repository.AuditRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория аудита в памяти
 * */
public class InMemoryAuditRepository implements AuditRepository {
    /**
     * Список сообщений аудита
     * */
    private final List<String> auditRepository = new ArrayList<>();

    @Override
    public void saveAudit(String username, String message) {
        auditRepository.add(username + ": " + message);
    }

    @Override
    public List<String> getAudit() {
        return auditRepository;
    }
}
