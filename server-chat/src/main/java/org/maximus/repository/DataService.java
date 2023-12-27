package org.maximus.repository;

public interface DataService {

    void saveChatToFile(String log, String path, boolean toAppend);
    String loadChatFromFile(String path);
}
