package application.service;

import application.port.in.MeterService;
import application.port.in.dto.MeterDataDTO;
import application.port.in.dto.MeterReadingDTO;
import application.port.in.exceptions.AccessDeniedException;
import application.port.repository.AuditRepository;
import application.port.repository.MeterDataRepository;
import application.port.repository.MeterTypeRepository;
import application.port.repository.UserMetersRepository;
import application.service.exceptions.NoSuchMeterTypeException;
import application.service.utils.UserValidationUtils;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.meterdata.ReadingData;
import model.meterdata.ReadingDate;
import model.user.User;

import java.time.YearMonth;
import java.util.List;

public class MeterServiceImpl implements MeterService {
    private final MeterDataRepository meterDataRepository;
    private final MeterTypeRepository meterTypeRepository;
    private final UserMetersRepository userMetersRepository;
    private final AuditRepository auditRepository;

    public MeterServiceImpl(MeterDataRepository meterDataRepository, MeterTypeRepository meterTypeRepository, UserMetersRepository userMetersRepository, AuditRepository auditRepository) {
        this.meterDataRepository = meterDataRepository;
        this.meterTypeRepository = meterTypeRepository;
        this.userMetersRepository = userMetersRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Получить список доступных типов счетчиков
     *
     * @return список типов счетчиков
     */
    @Override
    public List<MeterType> getAccessibleMeterTypes() {
        return meterTypeRepository.getMeterTypes();
    }

    /**
     * Получение истории счётчиков пользователя
     *
     * @param username     имя пользователя
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

    /**
     * Получить показания счетчиков за определенный месяц
     *
     * @param month        месяц
     * @param year         год
     * @param username     имя пользователя, чьи показания нужно получить
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
     *
     * @param username     имя пользователя, чьи показания нужно получить
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

    /**
     * Записать показание счетчика
     *
     * @param meterType    тип счетчика
     * @param readingValue показание счетчика
     * @param username     имя пользователя
     * @param loggedInUser данные о текущем пользователе
     * @throws DuplicateReadingException  если показание счетчика за этот месяц уже записано
     * @throws WrongReadingValueException если показание счетчика меньше, чем предыдущее показание
     * @throws AccessDeniedException      если у пользователя нет доступа к данным пользователя
     * @throws NoSuchMeterTypeException   если тип счетчика не найден
     */
    @Override
    public void writeMeterReading(MeterType meterType, int readingValue, String username, User loggedInUser) throws DuplicateReadingException, WrongReadingValueException, AccessDeniedException, NoSuchMeterTypeException {
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " tried to write meter reading for " + username + " for meter type " + meterType + " with value " + readingValue);

        if (!UserValidationUtils.haveAccessToUser(username, loggedInUser)) {
            throw new AccessDeniedException();
        }

        if (meterTypeRepository.findMeterType(meterType).isEmpty()) {
            throw new NoSuchMeterTypeException();
        }

        MeterData meterData = userMetersRepository.getUserMetersByUsername(username).getMeters()
                .stream()
                .filter(meter -> meter.getMeterType().equals(meterType))
                .findFirst()
                .orElse(new MeterData(meterType));

        meterData.addReading(new ReadingData(readingValue));

        meterDataRepository.putMeterData(meterData);
        userMetersRepository.putUserMeterByUsername(username, meterData);

        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " wrote meter reading for " + username + " for meter type " + meterType + " with value " + readingValue);
    }

    @Override
    public void addNewMeterType(String meterTypeName, User loggedInUser) throws AccessDeniedException {
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " tried to add new meter type " + meterTypeName);

        if (!UserValidationUtils.isAdmin(loggedInUser)) {
            throw new AccessDeniedException();
        }

        var meterType = new MeterType(meterTypeName);
        meterTypeRepository.addMeterType(meterType);

        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " added new meter type " + meterType);
    }
}
