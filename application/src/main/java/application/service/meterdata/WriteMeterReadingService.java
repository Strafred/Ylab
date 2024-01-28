package application.service.meterdata;

import application.port.in.meterdata.WriteMeterReadingUseCase;
import application.port.in.meterdata.exceptions.AccessDeniedException;
import application.port.out.AuditRepository;
import application.port.out.MeterDataRepository;
import application.port.out.MeterTypeRepository;
import application.port.out.UserMetersRepository;
import application.service.meterdata.exceptions.NoSuchMeterTypeException;
import application.service.utils.UserValidationUtils;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.meterdata.ReadingData;
import model.meterdata.exceptions.DuplicateReadingException;
import model.meterdata.exceptions.WrongReadingValueException;
import model.user.User;

/**
 * Сервис для записи показания счетчика
 */
public class WriteMeterReadingService implements WriteMeterReadingUseCase {
    MeterDataRepository meterDataRepository;
    UserMetersRepository userMetersRepository;
    MeterTypeRepository meterTypeRepository;
    AuditRepository auditRepository;

    public WriteMeterReadingService(MeterDataRepository meterDataRepository, UserMetersRepository userMetersRepository, MeterTypeRepository meterTypeRepository, AuditRepository auditRepository) {
        this.meterDataRepository = meterDataRepository;
        this.userMetersRepository = userMetersRepository;
        this.meterTypeRepository = meterTypeRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Записать показание счетчика
     * @param meterType тип счетчика
     * @param readingValue показание счетчика
     * @param username имя пользователя
     * @param loggedInUser данные о текущем пользователе
     * @throws DuplicateReadingException если показание счетчика за этот месяц уже записано
     * @throws WrongReadingValueException если показание счетчика меньше, чем предыдущее показание
     * @throws AccessDeniedException если у пользователя нет доступа к данным пользователя
     * @throws NoSuchMeterTypeException если тип счетчика не найден
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
}
