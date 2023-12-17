package org.example.model;

import java.util.Random;

public class RepositoryCourses {

    private static final String[] name = new String[] { "Русский язык", "Литература", "Математика", "Физика",
            "Химия", "Английский", "Астрономия", "Естествознание", "География", "Физкультура","Музыка"};
    private static final Random random = new Random();

    public static Course coursesCreator(){
        String duration = 3+random.nextInt(5) + ":" + random.nextInt(45);
        Course course = new Course(name[random.nextInt(name.length)],duration);
        return course;
    }

    public static void main(String[] args) {

    }
}
