package org.maximus;

import org.maximus.gui.ClientExchangable;

import java.io.*;
import java.net.Socket;


public class Client {

    private final Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private ClientExchangable clientExchangable;


    public Client(Socket socket, ClientExchangable clientExchangable) {
        this.socket = socket;
        this.clientExchangable = clientExchangable;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeAll();
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();
                        if (message.equals("Сервер был отключен!!"))
                        {
                            clientExchangable.getMessage(message + "\n");
                            sendMessage("emergency", "server stopped");
                           throw new IOException();
                        }
                        if (message == null) {
                            // для  Linux
                                closeAll();
                            throw new IOException();
                        }
                        System.out.println(message);
                        clientExchangable.getMessage(message + "\n");
                    } catch (IOException e) {
                        System.out.println("Unable to receive message");
                        clientExchangable.changeFormOnBreakConnection();
                        closeAll();
                        break;
                    }
                }
            }
        }).start();
    }

    public void sendMessage(String name, String message) {
        if (socket.isConnected()) {
            try {
                messageSender(name + ": " + message);
            } catch (IOException e) {
                clientExchangable.changeFormOnBreakConnection();
                closeAll();
            }
        }
    }

    private void messageSender(String message) throws IOException {
       bufferedWriter.write(message);
       bufferedWriter.newLine();
       bufferedWriter.flush();
    }

    public void closeAll() {

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connectToServer(String nameString, String passwd) {
        if (!socket.isConnected()) {
            return false;
        }
        try {
            messageSender(nameString);
            messageSender(passwd);
            StringBuilder stringBuilder = new StringBuilder();
            String readPart = bufferedReader.readLine();
            if (readPart.startsWith("Login successful!")) {
                String str;
                while (!(str = bufferedReader.readLine()).isEmpty()) {
                    stringBuilder.append(str).append('\n');
                }
                clientExchangable.chatLoad(stringBuilder.toString());
                clientExchangable.changeFieldsVisibility(true);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;

    }

    public void disconnectFromServer() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

