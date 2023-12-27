package org.maximus.gui;

import org.maximus.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;

public class ServerGUI extends JFrame implements ServerExchangable {

    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_POSX = 1307;
    private static final int WINDOW_POSY = 300;

    private static final String START_SERVER = "Start server";
    private static final String STOP_SERVER = "Stop server";
    private static final String CHAT_AREA = "Test logins: \nIvan \nPetr \nDanil \npasswords are '555'";

    private final int port = 7777;


    Server server;
    JButton btnStart;
    JButton btnStop;
    JTextArea textAreaChat;
    Thread threadServer;
//    AccessRepository repository;


    public ServerGUI() throws HeadlessException {
//        this.repository = repository;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Server window");
        setResizable(true);
        add(createLowPanel(), BorderLayout.SOUTH);
        add(createChatArea());
        setVisible(true);

    }

    private Component createChatArea() {
        textAreaChat = new JTextArea(CHAT_AREA);
        JScrollPane scroll = new JScrollPane (textAreaChat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        textAreaChat.setLineWrap(true);
        textAreaChat.setEditable(false);
//        JScrollPane y = new JScrollPane( textAreaChat,
//                VERTICAL_SCROLLBAR_ALWAYS ,
//                HORIZONTAL_SCROLLBAR_ALWAYS);
        return scroll;
    }

    private Component createLowPanel() {
        JPanel serverActions = new JPanel(new GridLayout(1, 3));
        createButtons();
        serverActions.add(btnStart);
        serverActions.add(btnStop);
        return serverActions;
    }

    private void createButtons() {
        btnStart = new JButton(START_SERVER);
        btnStop = new JButton(STOP_SERVER);
        btnStop.setEnabled(false);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                server.setServerIsOn(true);
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException err) {
                    System.out.println("Не удалось запустить сервер");
                    return;
//                    throw new RuntimeException(err);
                }
                server = new Server(serverSocket,port,ServerGUI.this);
                threadServer = new Thread(server);
                threadServer.start();
                textAreaChat.setText(server.loadChat());
//                server.runServer();
                System.out.println("Server is started");

                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
            }
        });
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStart.setEnabled(true);
                getAllThreads();

                System.out.println("Server is stopped");
                server.stopServer();
                btnStop.setEnabled(false);

            }
        });
    }

    public void getAllThreads(){
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        System.out.printf("%-15s \t %-15s \t %-15s \t %s\n", "Name", "State", "Priority", "isDaemon");
        for (Thread t : threads) {
            System.out.printf("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isDaemon());
        }
    }

    @Override
    public void addChatText(String message) {
        textAreaChat.append(message + "\n");
        server.saveChat(message+"\n");
    }


}
