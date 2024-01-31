package application.service.meterdata;

import application.port.in.meterdata.ShowMonthReadingsUseCase;
import application.port.in.meterdata.dto.MeterReadingDTO;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.service.utils.UserValidationUtils;
import model.meterdata.ReadingDate;
import model.user.User;

import java.time.YearMonth;
import java.util.List;

/**
 * Сервис для получения показаний счетчиков за определенный месяц
 */
public class ShowMonthReadingsService implements ShowMonthReadingsUseCase {
    UserMetersRepository userMetersRepository;
    AuditRepository auditRepository;

    public ShowMonthReadingsService(UserMetersRepository userMetersRepository, AuditRepository auditRepository) {
        this.userMetersRepository = userMetersRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Получить показания счетчиков за определенный месяц
     * @param month месяц
     * @param year год
     * @param username имя пользователя, чьи показания нужно получить
     * @param loggedInUser пользователь, который запрашивает показания
     * @return список показаний счетчиков
     * @throws AccessDeniedException если у пользователя нет доступа к запрашиваемому пользователю
     */
    @Override
    public List<MeterReadingDTO> getUsersSpecificMonthReadings(int month, int year, String username, User loggedInUser) throws AccessDeniedException {
        auditRepository.saveAudit(loggedInUser.getUsername(), "User " + loggedInUser.getUsername() + " requested " + username + "'s readings for " + month + "/" + year);

        if (!UserValidationUtils.haveAccessToUser(username, loggedInUser)) {
            throw new AccessDeniedException();
        }

        ReadingDate readingDate = new ReadingDate(year, month);

        auditRepository.saveAudit(loggedInUser.getUsername(), "User " + loggedInUser.getUsername() + " accessed " + username + "'s readings for " + month + "/" + year);
        return userMetersRepository.getUserMetersByUsername(username).getMeters()
                .stream()
                .filter(meter -> meter.getAllReadings().containsKey(readingDate))
                .map(meter -> new MeterReadingDTO(meter.getMeterType(), readingDate, meter.getAllReadings().get(readingDate)))
                .toList();
    }

    /**
     * Получить показания счетчиков за текущий месяц
     * @param username имя пользователя, чьи показания нужно получить
     * @param loggedInUser пользователь, который запрашивает показания
     * @return список показаний счетчиков
     * @throws AccessDeniedException если у пользователя нет доступа к запрашиваемому пользователю
     */
    @Override
    public List<MeterReadingDTO> getUsersCurrentMonthReadings(String username, User loggedInUser) throws AccessDeniedException {
        YearMonth currentYearMonth = YearMonth.now();
        var currentYear = currentYearMonth.getYear();
        var currentMonth = currentYearMonth.getMonthValue();

        return getUsersSpecificMonthReadings(currentMonth, currentYear, username, loggedInUser);
    }
}
