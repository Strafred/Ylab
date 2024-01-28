package application.service.meterdata;

import application.port.in.meterdata.ShowMetersHistoryUseCase;
import application.port.in.meterdata.dto.MeterDataDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.service.utils.UserValidationUtils;
import model.user.User;

import java.util.List;

/**
 * Сервис для получения истории счётчиков пользователя
 */
public class ShowMetersHistoryService implements ShowMetersHistoryUseCase {
    UserMetersRepository userMetersRepository;
    AuditRepository auditRepository;

    public ShowMetersHistoryService(UserMetersRepository userMetersRepository, AuditRepository auditRepository) {
        this.userMetersRepository = userMetersRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Получение истории счётчиков пользователя
     * @param username имя пользователя
     * @param loggedInUser авторизованный пользователь
     * @return список DTO с данными о счётчиках
     * @throws AccessDeniedException если авторизованный пользователь не имеет доступа к данным пользователя
     */
    @Override
    public List<MeterDataDTO> getMetersHistory(String username, User loggedInUser) throws AccessDeniedException {
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " tries to show meters history for user " + username);

        if (!UserValidationUtils.haveAccessToUser(username, loggedInUser)) {
            throw new AccessDeniedException();
        }

        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " accessed meters history for user " + username);
        return userMetersRepository.getUserMetersByUsername(username).getMeters()
                .stream()
                .map(meter -> new MeterDataDTO(meter.getMeterType(), meter.getAllReadings()))
                .toList();
    }
}
