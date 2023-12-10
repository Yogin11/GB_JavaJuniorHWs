package ru.geekbrains.lesson3.hw3;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class  IOprocessor {
    public static List<?> listToSAve;
    public static String fName;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * Сохранение (сериализация) данных в файл
     * @param filename нзвание файла без расширения
     * @param type тип файла в соответствиис перечислением FileTypes
     * @param lts список объектов   для сохранения
     * @param <T> тип объектов списка
     */
    public static <T> void dataSaver(String filename, FileTypes type, List<T> lts) {
        fName = filename;
        listToSAve = lts;

        try {
            switch (type){
                case BIN -> binarySave();
                case JSON -> jsonSave();
                case XML -> xmlSave();
            }
        } catch (IOException e) {
            System.out.println("Не удалось сохранить данные в файл");
        }
    }

    private static void binarySave() throws IOException {
         try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fName+".bin"))) {
             oos.writeObject(listToSAve);
         }
     }

    private static void jsonSave() throws IOException {
         objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
         objectMapper.writeValue(new File(fName+".json"), listToSAve);
    }

    private static void xmlSave() throws IOException {
         xmlMapper.writeValue(new File(fName+".xml"), listToSAve);
    }

    /**
     * Извлечение (десериализация) данных из файла
     * @param fileName полное название файла
     * @param item экземпляр элементарного объекта, извлекаемого из файла
     * @return извлекаемый список с объектами
     * @param <T> тип объекта
     */

    public static <T> List<T> loadDataFromFile(String fileName, T item) {
        File file = new File(fileName);
        Class<?> clazz = item.getClass();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        try {
            switch (extension){
                case "bin" -> {return binaryLoad(file);}
                case "json" -> {return jsonLoad(file, clazz);}
                case "xml" -> {return xmlLoad(file, clazz);}
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Не удалось загрузить данные  из файла");
        }
        return null;
    }

    private static <T> List<T> binaryLoad(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        }
    }

    private static <T> List<T> jsonLoad(File file, Class<?> aClass) throws IOException {
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, aClass));

    }

    private static <T> List<T> xmlLoad(File file, Class<?> aClass) throws IOException {
        return xmlMapper.readValue(file, xmlMapper.getTypeFactory().constructCollectionType(ArrayList.class, aClass));
    }

}

