package ru.geekbrains.lesson1;


import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class HW1 {
    /*
    Напишите программу, которая использует Stream API для обработки списка чисел.
    Программа должна вывести на экран среднее значение всех четных чисел в списке.
     */
    public static void main(String[] args) {

        List<Integer> list = listGenerator();
        list.stream()
                .filter((n) -> n%2==0)
                .mapToInt(Integer::valueOf)
                .average()
                .ifPresent(System.out::println);
    }
    public static List<Integer> listGenerator(){
        List<Integer> list = Stream.generate(()-> new Random().nextInt(1,10))
                .limit(7)
                .toList();
        System.out.println(list);
        return list;
    }


}
