package ru.geekbrains.lesson2.hw2;

public class Program {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException {

        Animal[] arrayAnimal = new Animal[]{
                new Cat("Барсик", 7, "наглый"),
                new Cat("Маруся", 10, "подушечная"),
                new Dog("Волга", 12, "дворняга"),
                new Dog("Амур", 3, "лабрадор"),
                new Dog("Шерхан", 2, "овчарка")
        };
        new Informer().showObjects(arrayAnimal);
    }
}
