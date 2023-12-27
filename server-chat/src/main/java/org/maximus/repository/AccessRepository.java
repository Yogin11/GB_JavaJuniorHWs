package org.maximus.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class AccessRepository implements ServerAccessable, DataService {
    private static HashMap<String, String> access;

    public AccessRepository() {
        access = new HashMap<>();
        access.put("Ivan", "555");
        access.put("Danil", "555");
        access.put("Petr", "555");
    }

    public boolean loginVerification(String nameString, String password) {
        if (access.containsKey(nameString)) {
            return access.get(nameString).equals(password);
        }
        return false;
    }

    @Override
    public void saveChatToFile(String log, String path, boolean toAppend) {
        try (FileOutputStream tofile = new FileOutputStream(path, toAppend);
             BufferedOutputStream stream = new BufferedOutputStream(tofile)) {
            stream.write(log.getBytes());
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public String loadChatFromFile(String path) {
        File file = new File(path);
        System.out.println(path);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String str;
            while ((str = in.readLine()) != null) {
                stringBuilder.append(str).append("\n");
            }
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
        return stringBuilder.toString();
    }
}



