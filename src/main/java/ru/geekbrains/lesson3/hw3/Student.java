package ru.geekbrains.lesson3.hw3;

import java.beans.Transient;
import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private int age;
    private transient double GPA;  //скрываем от сериализации в бинарный файл

    public Student() {
    }

    public Student(String name, int age, double GPA) {
        this.name = name;
        this.age = age;
        this.GPA = GPA;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Transient  // Требуется для несериализации значения при использовании библиотеки Jackson (Json и Xml)
    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    @Override
    public String toString() {
        return name + ", " + age + " лет, GPA = " + GPA;
    }
}
