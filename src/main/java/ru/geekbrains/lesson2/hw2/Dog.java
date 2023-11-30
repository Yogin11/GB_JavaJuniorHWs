package ru.geekbrains.lesson2.hw2;

public class Dog extends Animal{
    private String breed;

    public Dog(String name, int age, String breed ) {
       this.name = name;
       this.age = age;
       this.breed = breed;
    }
//    public void makeSound(){
//        System.out.println("Гав Гав!!");
//    }
    public void commandSit(){
        System.out.println(name + " выполняет команду 'сидеть' ");
    }
    public void commandBring(){
        System.out.println(name + "  выполняет команду 'принести' ");
    }

      public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }


}
