package org.maximus;


import org.maximus.gui.ServerGUI;
import org.maximus.repository.*;
import org.maximus.service.*;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private ServerSocket serverSocket;
    private final DataService repository;
    private final ServerAccessable accessRepo;
    private final ServerGUI serverGUI;
    private static String chatFile = "server-chat/src/main/resources/chat.txt";

    public Server (ServerSocket serverSocket, ServerGUI serverG){
        this.serverSocket = serverSocket;
        this.repository = new AccessRepository();
        this.accessRepo = new AccessRepository();
        this.serverGUI= serverG;

        System.out.println(serverSocket.getLocalSocketAddress());
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            runServer();
        }
    }

    public void runServer(){
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                 ClientManager clientManager = new ClientManager(socket, this, serverGUI, accessRepo);
                Thread thread = new Thread(clientManager);
                thread.start();
            }
        }
        catch (IOException e){
            closeSocket();
        }
    }

    public void stopServer(){
        ClientManager.announceToAllClients();

        ClientManager.clients
            .forEach(cl->{
                cl.setLoggedOn(false);
                cl.closeAll();
            });
        ClientManager.clients.clear();
        closeSocket();
    }


    private void closeSocket(){
        try{
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String loadChat() {
        return repository.loadChatFromFile(chatFile);
    }

    public void saveChat(String log){
        repository.saveChatToFile(log, chatFile,true);
    }


}
