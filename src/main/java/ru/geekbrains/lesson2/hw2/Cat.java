package ru.geekbrains.lesson2.hw2;

import javax.swing.*;

public class Cat extends Animal {
    private String specialCatField;

    public Cat(String name, int age, String specialCatField) {
        this.name = name;
        this.age = age;
        this.specialCatField = specialCatField;

    }
    public void makeSound(){
        System.out.println("Мяяааауу ");
    }
    public void purrs(){
        System.out.println(name + " урчит ");
    }

    public String getSpecialCatField() {
        return specialCatField;
    }

    public void setSpecialCatField(String specialCatField) {
        this.specialCatField = specialCatField;
    }
}
