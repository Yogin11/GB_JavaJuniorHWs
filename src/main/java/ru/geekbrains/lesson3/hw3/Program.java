package ru.geekbrains.lesson3.hw3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        List<Student> studentList = new ArrayList<>();

        //Выберите файл
//        String filename =  "datafile.bin";
//        String filename =  "datafile.json";
        String filename =  "datafile.xml";

        if (new File(filename).exists() && !new File(filename).isDirectory()) {
            studentList = IOprocessor.loadDataFromFile(filename, new Student());
            System.out.println("Загруженный из файла '" + filename + "' список студентов : ");
        }
        else {
            studentList = addStudents();
            for (FileTypes type : FileTypes.values()) {
                IOprocessor.dataSaver("datafile", type, studentList);
            }
            System.out.println("Вновь созданный список студентов : ");
        }
        printer(studentList);
    }

    public static void printer(List<?> list){
        list.forEach(System.out::println);
    }

    /**
     * добавляем абстрактных студентов
     * @return список студентов
     */
    public static List<Student> addStudents(){
        List<Student> list = new ArrayList<>();
        list.add(new Student("Сергей Капица", 18, 4.3));
        list.add(new Student("Елена Блаватская ", 17, 4.7));
        list.add(new Student("Георгий Гурджиев", 18, 4.8));

        return list;
    }

}
