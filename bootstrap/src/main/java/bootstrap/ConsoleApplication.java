package bootstrap;

import application.port.in.MeterService;
import application.port.in.UserService;
import application.port.out.*;
import application.service.MeterServiceImpl;
import application.service.UserServiceImpl;
import model.meterdata.MeterType;
import model.user.User;
import ylab.adapter.in.MeterController;
import ylab.adapter.in.UserController;
import ylab.adapter.out.*;

import java.util.Scanner;

/**
 * Класс для запуска консольного приложения
 */
public class ConsoleApplication {
    /**
     * Поле для хранения залогиненного пользователя
     */
    private static User loggedInUser;

    /**
     * Точка входа в приложение
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        AuditRepository auditRepository = new InMemoryAuditRepository();

        UserRepository userRepository = new InMemoryUserRepository();
        UserMetersRepository userMetersRepository = new InMemoryUserMetersRepository();
        UserService userService = new UserServiceImpl(userRepository, userMetersRepository, auditRepository);
        UserController userController = new UserController(userService);


        MeterDataRepository meterDataRepository = new InMemoryMeterDataRepository();
        MeterTypeRepository meterTypeRepository = new InMemoryMeterTypeRepository();
        MeterService meterService = new MeterServiceImpl(meterDataRepository, meterTypeRepository, userMetersRepository, auditRepository);
        MeterController meterController = new MeterController(meterService);


        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerUser(scanner, userController);
                case 2 -> loginUser(scanner, userController);
                case 3 -> writeMeterReading(scanner, meterController);
                case 4 -> showMonthReadings(scanner, meterController);
                case 5 -> getUsersSpecificMonthReadings(scanner, meterController);
                case 6 -> showMetersHistory(scanner, meterController);
                case 7 -> System.exit(0);
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Показать пользователю историю счетчиков выбранного пользователя (если есть права)
     * @param scanner сканнер для ввода данных
     * @param meterController контроллер для работы с счетчиками
     */
    private static void showMetersHistory(Scanner scanner, MeterController meterController) {
        System.out.println("Введите пользователя:");
        String username = scanner.nextLine();

        System.out.println(meterController.showMetersHistory(username, loggedInUser));
    }

    /**
     * Показать пользователю показания счетчиков выбранного пользователя за выбранный месяц (если есть права)
     * @param scanner сканнер для ввода данных
     * @param meterController контроллер для работы с счетчиками
     */
    private static void getUsersSpecificMonthReadings(Scanner scanner, MeterController meterController) {
        System.out.println("Введите месяц:");
        int month = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Введите год:");
        int year = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Введите пользователя:");
        String username = scanner.nextLine();

        System.out.println(meterController.getUsersSpecificMonthReadings(month, year, username, loggedInUser));
    }

    /**
     * Показать показания счетчиков за настоящий месяц
     * @param scanner сканнер для ввода данных
     * @param meterController контроллер для работы с счетчиками
     */
    private static void showMonthReadings(Scanner scanner, MeterController meterController) {
        System.out.println("Введите пользователя:");
        String username = scanner.nextLine();

        System.out.println(meterController.showMonthReadings(username, loggedInUser));
    }

    /**
     * Вписать показание счетчика
     * @param scanner сканнер для ввода данных
     * @param meterController контроллер для работы с счетчиками
     */
    private static void writeMeterReading(Scanner scanner, MeterController meterController) {
        if (loggedInUser == null) {
            System.out.println("Сначала авторизуйтесь!");
            return;
        }
        System.out.println("Выберите счетчик:");
        int i = 1;
        for (MeterType meterType : meterController.getAccessibleMeterTypes()) {
            System.out.println(i + ". " + meterType.getName());
            i++;
        }

        int meterTypeIndex = scanner.nextInt();
        scanner.nextLine();
        MeterType selectedMeterType = meterController.getAccessibleMeterTypes().get(meterTypeIndex - 1);

        System.out.println("Введите показание:");
        int reading = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Введите пользователя:");
        String username = scanner.nextLine();

        meterController.writeMeterReading(selectedMeterType, reading, username, loggedInUser);
    }

    /**
     * Авторизовать пользователя
     * @param scanner сканнер для ввода данных
     * @param userController контроллер для работы с пользователями
     */
    private static void loginUser(Scanner scanner, UserController userController) {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        loggedInUser = userController.loginUser(username, password);
    }

    /**
     * Зарегистрировать пользователя
     * @param scanner сканнер для ввода данных
     * @param userController контроллер для работы с пользователями
     */
    private static void registerUser(Scanner scanner, UserController userController) {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        userController.registerUser(username, password);
    }

    /**
     * Вывести меню
     */
    private static void displayMenu() {
        System.out.println("1. Регистрация");
        System.out.println("2. Авторизация");
        System.out.println("3. Подача показания");
        System.out.println("4. Актуальные показания");
        System.out.println("5. Просмотр показаний за конкретный месяц");
        System.out.println("6. Просмотр истории подачи показаний");
        System.out.println("7. Выход");
    }
}
