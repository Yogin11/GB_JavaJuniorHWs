package ru.geekbrains.lesson2.hw2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Informer {

    public void showObjects(Object[] array) throws IllegalAccessException, ClassNotFoundException {
        int counter = 0;
        for (Object arrayItem : array) {
            Class<?> clazz = Class.forName(arrayItem.getClass().getName());
            System.out.println("В Объекте " + ++counter + " " + clazz.getSimpleName());
            showConstructors(clazz);
            showFields(clazz, arrayItem);
            showMethods(clazz);
            implementMethod(clazz, arrayItem);
        }
    }

    /**
     * Выводим на экран все конструкторы класса объекта
     *
     * @param clazz класс
     */
    private void showConstructors(Class<?> clazz) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.print("Constructor: " + constructor.toString() + " ");
        }
        System.out.println();
    }

    /**
     * Выводим на экран все методы класса объекта
     *
     * @param clazz класс объекта
     */

    private void showMethods(Class<?> clazz) {
        System.out.print("Методы: ");
        Method[] methodsAnimal = clazz.getDeclaredMethods();
        for (Method method : methodsAnimal) {
            System.out.print(method.getName() + "; ");
//                if (method.getName().equals("makeSound")){
//                       method.invoke(arrayItem);                }
        }
        System.out.println();
    }

    /**
     *  Выводим на экран все поля объекта со значениями
     *
     * @param clazz класс объекта
     * @param obj   объект
     * @throws IllegalAccessException
     */

    private void showFields(Class<?> clazz, Object obj) throws IllegalAccessException {
        // Собираем все поля, в т.ч. унаследованные от родительского класса
        List<Field> allFields = new ArrayList<>();
        allFields.addAll(List.of(clazz.getSuperclass().getDeclaredFields()));
        allFields.addAll(List.of(clazz.getDeclaredFields()));
        for (Field field : allFields) {
            field.setAccessible(true);
            System.out.println("Поле: " + field.getName() + " = '" + field.get(obj) + "' ");
        }
    }

    /**
     * Запускаем метод "makeSound" для объекта
     *
     * @param clazz класс объекта
     * @param obj   объект
     */

    private void implementMethod(Class<?> clazz, Object obj) {
        //Метод "makeSound"
        try {
            Method displayMakeSoundMethod = clazz.getDeclaredMethod("makeSound");
            System.out.print("Запуск метода 'makeSound': ");
            displayMakeSoundMethod.invoke(obj);
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("Нет метода 'makeSound' в классе " + clazz.getSimpleName());
        }
        System.out.println();
    }

}