package application.port.in.meterdata;

import application.port.in.meterdata.dto.MeterDataDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import model.user.User;

import java.util.List;

/**
 * Сценарий: просмотр истории показаний счетчиков
 */
public interface ShowMetersHistoryUseCase {
    /**
     * Просмотр истории показаний счетчиков
     * @param username - имя пользователя
     * @param loggedInUser - текущий пользователь
     * @return - список показаний счетчиков
     * @throws AccessDeniedException - если текущий пользователь не имеет права просматривать историю показаний счетчиков пользователя
     */
    List<MeterDataDTO> getMetersHistory(String username, User loggedInUser) throws AccessDeniedException;
}
