package org.example;

import org.example.DBFlow.HiberConnector;

import java.util.Scanner;

public class Menu {
    Scanner scanner = new Scanner(System.in);
    HiberConnector hiberConnector = new HiberConnector();

    public void getMenu() {

        while (true) {
            System.out.println("================================================================");
            System.out.println("0 - Завершение работы");
            System.out.println("1 - Загрузить курсы в базу");
            System.out.println("2 - Отобразить полный список курсов");
            System.out.println("3 - Найти курс по Id");
            System.out.println("4 - Добавить новый курс");
            System.out.println("5 - Изменить данные курса");
            System.out.println("6 - Удалить курс");

            System.out.println("Выберите пункт меню: ");

            if (scanner.hasNextInt()) {
                int no = scanner.nextInt();
                scanner.nextLine();
                switch (no) {
                    case 0 -> {
                        System.out.println("Завершение работы приложения.");
                        return;
                    }
                    case 1 -> createCourses();
                    case 2 -> showAllRecords();
                    case 3 -> findCourse();
                    case 4 -> addCourse();
                    case 5 -> updateCourse();
                    case 6 -> deleteCourse();
                    default -> System.out.println("Пункт меню не существует.\nПожалуйста, повторите попытку ввода.");
                }
            } else {
                System.out.println("Некорректный пункт меню.\nПожалуйста, повторите попытку ввода.");
                scanner.nextLine();
            }
        }
    }

    private void showAllRecords() {
        hiberConnector.actions("2", 0);
    }

    private void createCourses() {
        hiberConnector.actions("1", 0);
    }

    private String findCourse() {
        return actById("3");
    }

    private void addCourse() {
        String name = getName();
        String duration = getDuration();
        if (!duration.isEmpty()){
            hiberConnector.actions("4", 0, name, duration);
            System.out.println("Курс добавлен");
        }

    }

    private String getName() {
        System.out.println("Введите название курса");
        return scanner.next();
    }

    private String getDuration() {
        System.out.println("Введите длительность курса в формате чч:мм ");
        String duration = scanner.next();
        String regex = "^(\\d{1,2}):(\\d{1,2})$";
        if (duration.matches(regex)) return duration;
        System.out.println("Неправильный формат продолжительности");
        return "";
    }

    private void updateCourse() {
        int searchResult = Integer.parseInt(findCourse());
        System.out.println("Введите какую информацию нужно изменить: ");
        System.out.println("1 - Название");
        System.out.println("2 - Длительность курса");
        System.out.println("3 - Все данные");
        String choice = scanner.next();
        if (choice.equals("1")) {
            hiberConnector.actions("5", searchResult, getName(), "");
        } else if (choice.equals("2")) {
            hiberConnector.actions("5", searchResult, "", getDuration());
        } else if (choice.equals("3")) {
            hiberConnector.actions("5", searchResult, getName(), getDuration());
        } else {System.out.println("Неверный выбор");return;}
        System.out.println("Курс мзменен");
    }

    private void deleteCourse() {
        actById("6");
        System.out.println("Курс удален");
    }

    private String actById(String choice) {
        System.out.println("Введите id курса");
        String str = scanner.next();
        if (checkInt(str)) {
            int input = Integer.valueOf(str);
            hiberConnector.actions(choice, input);
            return str;
        } else {
            System.out.println("Некорректный ввод");
        }
        return "";
    }

    private boolean checkInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
