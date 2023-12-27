package org.maximus.gui;

public interface ClientExchangable {
    void getMessage(String message);

    void changeFieldsVisibility(boolean status);

    void chatLoad(String loadedChat);

    void changeFormOnBreakConnection();
}
