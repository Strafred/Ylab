package bootstrap;

import application.port.in.MeterService;
import application.port.in.UserService;
import application.port.repository.*;
import application.service.MeterServiceImpl;
import application.service.UserServiceImpl;
import io.github.cdimascio.dotenv.Dotenv;
import model.meterdata.MeterType;
import model.user.User;
import ylab.adapter.in.MeterController;
import ylab.adapter.in.UserController;
import ylab.adapter.repository.postgresql.*;

import java.sql.Connection;
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
     * Ссылка на базу данных
     */
    private static String URL = "jdbc:postgresql://localhost:5432/";

    /**
     * Имя пользователя для подключения к базе данных
     */
    private static String USER_NAME;

    /**
     * Пароль для подключения к базе данных
     */
    private static String PASSWORD;

    /**
     * Точка входа в приложение
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        URL += dotenv.get("POSTGRES_DB");
        USER_NAME = dotenv.get("POSTGRES_USER");
        PASSWORD = dotenv.get("POSTGRES_PASSWORD");

        Connection connection = BootstrapUtils.initPostgresConnection(URL, USER_NAME, PASSWORD);

        AuditRepository auditRepository = new PostgresAuditRepository(connection);

        UserRepository userRepository = new PostgresUserRepository(connection);
        UserMetersRepository userMetersRepository = new PostgresUserMetersRepository(connection);
        UserService userService = new UserServiceImpl(connection, userRepository, userMetersRepository, auditRepository);
        UserController userController = new UserController(userService);


        MeterDataReadingRepository meterDataReadingRepository = new PostgresMeterDataReadingRepository(connection);
        MeterDataRepository meterDataRepository = new PostgresMeterDataRepository(connection);
        MeterTypeRepository meterTypeRepository = new PostgresMeterTypeRepository(connection);
        MeterService meterService = new MeterServiceImpl(connection, meterDataReadingRepository, meterDataRepository, meterTypeRepository, userMetersRepository, auditRepository);
        MeterController meterController = new MeterController(meterService);


        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 0 -> addMeterType(scanner, meterController);
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
     * Добавить новый тип счётчика
     * @param scanner сканнер для ввода данных
     * @param meterController контроллер для работы с счетчиками
     */
    private static void addMeterType(Scanner scanner, MeterController meterController) {
        if (loggedInUser == null) {
            System.out.println("Сначала авторизуйтесь!");
            return;
        }

        System.out.println("Введите название типа счетчика:");
        var meterTypeName = scanner.nextLine();

        meterController.addNewMeterType(meterTypeName, loggedInUser);
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
            System.out.println(i + ". " + meterType.getMeterTypeName());
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
        System.out.println("0. Добавить новый тип счетчика (только для администратора)");
        System.out.println("1. Регистрация");
        System.out.println("2. Авторизация");
        System.out.println("3. Подача показания");
        System.out.println("4. Актуальные показания");
        System.out.println("5. Просмотр показаний за конкретный месяц");
        System.out.println("6. Просмотр истории подачи показаний");
        System.out.println("7. Выход");
    }
}
