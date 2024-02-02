package ylab.adapter.repository.inmemory;

import application.port.repository.UserMetersRepository;
import model.meterdata.MeterData;
import model.usermeter.UserMeters;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для работы с данными о счетчиках пользователей в памяти
 */
public class InMemoryUserMetersRepository implements UserMetersRepository {
    /**
     * Список данных о счетчиках пользователей
     */
    private final List<UserMeters> userMetersList = new ArrayList<>();

    @Override
    public void saveUserMeters(UserMeters userMeters) {
        int index = userMetersList.indexOf(userMeters);
        if (index != -1) {
            userMetersList.set(index, userMeters);
        } else {
            userMetersList.add(userMeters);
        }
    }

    @Override
    public UserMeters getUserMetersByUsername(String username) {
        return userMetersList.stream()
                .filter(userMeters -> userMeters.getUser().getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void putUserMeterByUsername(String username, MeterData meterData) {
        UserMeters userMeters = getUserMetersByUsername(username);

        if (userMeters != null) {
            userMeters.addMeter(meterData);
            saveUserMeters(userMeters);
        }
    }
}
