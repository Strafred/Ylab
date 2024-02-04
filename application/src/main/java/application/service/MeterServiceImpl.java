package application.service;

import application.port.in.MeterService;
import application.port.in.dto.MeterDataDTO;
import application.port.in.dto.MeterReadingDTO;
import application.port.in.exceptions.AccessDeniedException;
import application.port.repository.*;
import application.service.exceptions.NoSuchMeterTypeException;
import application.service.utils.UserValidationUtils;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongReadingValueException;
import model.exceptions.WrongUsernameException;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.meterdata.ReadingData;
import model.meterdata.ReadingDate;
import model.user.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class MeterServiceImpl implements MeterService {
    private final Connection connection;
    private final MeterDataReadingRepository meterDataReadingRepository;
    private final MeterDataRepository meterDataRepository;
    private final MeterTypeRepository meterTypeRepository;
    private final UserMetersRepository userMetersRepository;
    private final AuditRepository auditRepository;

    public MeterServiceImpl(Connection connection, MeterDataReadingRepository meterDataReadingRepository, MeterDataRepository meterDataRepository, MeterTypeRepository meterTypeRepository, UserMetersRepository userMetersRepository, AuditRepository auditRepository) {
        this.connection = connection;
        this.meterDataReadingRepository = meterDataReadingRepository;
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
    public List<MeterType> getAccessibleMeterTypes() throws SQLException {
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
    public List<MeterDataDTO> getMetersHistory(String username, User loggedInUser) throws AccessDeniedException, SQLException, WrongUsernameException, WrongPasswordException {
        connection.setAutoCommit(false);
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " tries to show meters history for user " + username);
        connection.commit();

        if (!UserValidationUtils.haveAccessToUser(username, loggedInUser)) {
            throw new AccessDeniedException();
        }

        connection.setAutoCommit(false);
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " accessed meters history for user " + username);
        connection.commit();

        return userMetersRepository.getUserMetersByUsername(username).getMeters()
                .stream()
                .map(meter -> {
                    try {
                        var readings = meterDataReadingRepository.getMeterDataReadingsByMeterData(meter).getAllReadings();
                        return new MeterDataDTO(meter.getMeterType(), readings);
                    } catch (SQLException | DuplicateReadingException e) {
                        System.err.println("Error while getting meter data readings for meter " + meter.getMeterType() + " for user " + username);
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
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
    public List<MeterReadingDTO> getUsersSpecificMonthReadings(int month, int year, String username, User loggedInUser) throws AccessDeniedException, SQLException, WrongUsernameException, WrongPasswordException {
        connection.setAutoCommit(false);
        auditRepository.saveAudit(loggedInUser.getUsername(), "User " + loggedInUser.getUsername() + " requested " + username + "'s readings for " + month + "/" + year);
        connection.commit();

        if (!UserValidationUtils.haveAccessToUser(username, loggedInUser)) {
            throw new AccessDeniedException();
        }

        ReadingDate readingDate = new ReadingDate(year, month);

        connection.setAutoCommit(false);
        auditRepository.saveAudit(loggedInUser.getUsername(), "User " + loggedInUser.getUsername() + " accessed " + username + "'s readings for " + month + "/" + year);
        connection.commit();

        return userMetersRepository.getUserMetersByUsername(username).getMeters()
                .stream()
                .filter(meter -> {
                    try {
                        var readings = meterDataReadingRepository.getMeterDataReadingsByMeterData(meter).getAllReadings();
                        return readings.containsKey(readingDate);
                    } catch (SQLException | DuplicateReadingException e) {
                        System.err.println("Error while getting meter data readings for meter " + meter.getMeterType() + " for user " + username);
                        e.printStackTrace();
                        return false;
                    }
                })
                .map(meter -> {
                    try {
                        var readings = meterDataReadingRepository.getMeterDataReadingsByMeterData(meter).getAllReadings();
                        return new MeterReadingDTO(meter.getMeterType(), readingDate, readings.get(readingDate));
                    } catch (SQLException | DuplicateReadingException e) {
                        System.err.println("Error while getting meter data readings for meter " + meter.getMeterType() + " for user " + username);
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
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
    public List<MeterReadingDTO> getUsersCurrentMonthReadings(String username, User loggedInUser) throws AccessDeniedException, WrongUsernameException, SQLException, WrongPasswordException {
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
    public void writeMeterReading(MeterType meterType, int readingValue, String username, User loggedInUser) throws DuplicateReadingException, WrongReadingValueException, AccessDeniedException, NoSuchMeterTypeException, SQLException, WrongUsernameException, WrongPasswordException {
        connection.setAutoCommit(false);
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " tried to write meter reading for " + username + " for meter type " + meterType + " with value " + readingValue);
        connection.commit();

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

        connection.setAutoCommit(false);
        meterData = meterDataRepository.postMeterData(meterData, username);
        meterDataReadingRepository.putNewReadingByMeterData(meterData, new ReadingData(readingValue));
        userMetersRepository.putUserMeterByUsername(username, meterData);
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " wrote meter reading for " + username + " for meter type " + meterType + " with value " + readingValue);
        connection.commit();
    }

    @Override
    public void addNewMeterType(String meterTypeName, User loggedInUser) throws AccessDeniedException, SQLException {
        connection.setAutoCommit(false);
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " tried to add new meter type " + meterTypeName);
        connection.commit();

        if (!UserValidationUtils.isAdmin(loggedInUser)) {
            throw new AccessDeniedException();
        }

        var meterType = new MeterType(meterTypeName);

        connection.setAutoCommit(false);
        meterTypeRepository.addMeterType(meterType);
        auditRepository.saveAudit(loggedInUser.getUsername(), loggedInUser.getUsername() + " added new meter type " + meterType);
        connection.commit();
    }
}
