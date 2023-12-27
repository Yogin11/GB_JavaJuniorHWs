package org.maximus.service;

import org.maximus.Server;
import org.maximus.gui.ServerExchangable;
import org.maximus.repository.ServerAccessable;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientManager implements Runnable {

    public static final ArrayList<ClientManager> clients = new ArrayList<>();

    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private boolean loggedOn;
    private ServerExchangable serverExchange;
    private ServerAccessable serverAccess;
    private Server server;
    private String name;

    public ClientManager(Socket socket, Server server, ServerExchangable serverExchange, ServerAccessable serverAccess) {
        this.socket = socket;
        loggedOn = false;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.serverExchange = serverExchange;
            this.serverAccess = serverAccess;
            this.server = server;
            name = bufferedReader.readLine();
            String password = bufferedReader.readLine();
            loginProcess(password);

        } catch (IOException e) {
            closeAll();
        }
    }

    public static void announceToAllClients() {
        ClientManager.clients.forEach(cl -> cl.broadcastMessage("Сервер был отключен!!"));
    }

    public void setLoggedOn(boolean loggedOn) {
        this.loggedOn = loggedOn;
    }

    public void loginProcess(String password) throws IOException {

        if (loginVerified(name, password)) {
            clients.add(this);
            System.out.println();
            System.out.println(name + " подключился к чату.");
            String enteredChat = "Server: " + name + " подключился к чату.";
            this.broadcastMessage(enteredChat);
            serverExchange.addChatText(enteredChat);
            messageSender(this, "Login successful!");
            messageSender(this, server.loadChat());
            loggedOn = true;
        } else {
            messageSender(this, "Неверный логин или пароль!");
            loggedOn = false;
            closeAll();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
//        bufferedReader.
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.equals("emergency: server stopped"))
                    break;
                if (messageFromClient == null) {
                    // для  Linux
                    closeAll();
                    break;
                }
                broadcastMessage(messageFromClient);
                if (messageFromClient.contains(" @")) continue;
                serverExchange.addChatText(messageFromClient);

            } catch (IOException e) {
                closeAll();
                break;
            }
        }
    }

    private void broadcastMessage(String message) {
        String prefix = name + ": @";
        for (ClientManager client : clients) {
            try {
                if (!client.name.equals(name)) {
                    if (message.startsWith(prefix)) {
                        if (message.startsWith(prefix + client.name)) {
                            int startPosition = (prefix + client.name).length();
                            String cleanMessage = name + "(~private~): " + message.substring((startPosition));
                            messageSender(client, cleanMessage);
                            break;
                        }
                    } else {
                        messageSender(client, message);
                    }
                }
            } catch (IOException e) {
                closeAll();
                throw new RuntimeException(e);
            }
        }
    }

    private void messageSender(ClientManager client, String message) throws IOException {
        client.bufferedWriter.write(message);
        client.bufferedWriter.newLine();
        client.bufferedWriter.flush();
    }


    public void closeAll() {
        if (loggedOn) removeClient();
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

    private void removeClient() {
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        String leftChat = "Server: " + name + " покинул чат";
        broadcastMessage(leftChat);
        serverExchange.addChatText(leftChat);
    }

    public boolean loginVerified(String nameString, String password) {
        return serverAccess.loginVerification(nameString, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientManager other = (ClientManager) o;
        return this.name.equals(other.name) &&
               socket.getInetAddress().equals(other.socket.getInetAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, server, name);
    }

}
