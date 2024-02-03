package model.usermeter;

import model.meterdata.MeterData;
import model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.UUID.randomUUID;

/**
 * Класс для хранения данных о пользователе и его счетчиках
 */
public class UserMeters {
    /**
     * Идентификатор
     */
    private int userMetersId;

    /**
     * Пользователь
     */
    private User user;
    /**
     * Список счетчиков
     */
    private List<MeterData> meters;

    /**
     * Конструктор
     * @param user пользователь
     */
    public UserMeters(User user) {
        this.userMetersId = randomUUID().hashCode();
        this.user = user;
        this.meters = new ArrayList<>();
    }

    /**
     * Конструктор
     * @param user пользователь
     * @param meters список счетчиков
     */
    public UserMeters(User user, List<MeterData> meters) {
        this.userMetersId = randomUUID().hashCode();
        this.user = user;
        this.meters = meters;
    }

    public UserMeters(int userMetersId, User user, List<MeterData> meters) {
        this.userMetersId = userMetersId;
        this.user = user;
        this.meters = meters;
    }

    /**
     * Добавить счетчик пользователю
     * @param meter счетчик
     */
    public void addMeter(MeterData meter) {
        meters.add(meter);
    }

    /**
     * Получить пользователя
     * @return пользователь
     */
    public User getUser() {
        return user;
    }

    /**
     * Получить список счетчиков
     * @return список счетчиков
     */
    public List<MeterData> getMeters() {
        return meters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMeters that = (UserMeters) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
