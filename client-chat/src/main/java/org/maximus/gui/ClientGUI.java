package org.maximus.gui;

import org.maximus.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class ClientGUI extends JFrame implements ClientExchangable {

    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_POSX = 800;
    private static final int WINDOW_POSY = 300;

    private final String addrString = "127.0.0.1";
    private final String portString = "7777";
    private final String nameString = "Ivan";
    private final String passwordString = "555";

    private  Socket socket;
    private Client client;
    private JTextField ipAddress, port, clientName, textSendField;
    private JPasswordField password;
    private JButton btnLogin, btnSend, btnExit;
    private JPanel panelLogin, panelSend;
    private JTextArea textAreaChat;
    private String loadChat;
    private String name;

    public ClientGUI() {
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Client window");
        setResizable(true);
        add(createChatArea());
        add(createPanelLogin(), BorderLayout.NORTH);
        add(createPanelSend(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private Component createChatArea() {
        textAreaChat = new JTextArea();
        JScrollPane scroll = new JScrollPane (textAreaChat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        textAreaChat.setLineWrap(true);
        textAreaChat.setEditable(false);
        textAreaChat.setEnabled(false);
        return scroll;
    }

    private Component createPanelLogin() {

        ipAddress = new JTextField(addrString, 15);
        port = new JTextField(portString, 5);
        clientName = new JTextField(nameString, 40);
        password = new JPasswordField(passwordString, 10);
        btnLogin = new JButton("login");
        createLoginListeners();
        panelLogin = new JPanel(new GridLayout(2, 3));
        panelLogin.add(ipAddress);
        panelLogin.add(port);

        btnExit = new JButton("Leave chat");
        panelLogin.add(btnExit);
        btnExit.setEnabled(false);
        createExitListener();
        panelLogin.add(clientName);
        panelLogin.add(password);
        panelLogin.add(btnLogin);
        return panelLogin;
    }

    private Component createPanelSend() {
        textSendField = new JTextField(40);
        btnSend = new JButton("send");
        createSendListeners();
        panelSend = new JPanel(new BorderLayout());
        panelSend.add(textSendField);
        panelSend.add(btnSend, BorderLayout.EAST);
        changeFieldsVisibility(false);
        return panelSend;
    }

    private void createLoginListeners() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pswd = new String(password.getPassword());
                try {
                    socket = new Socket(ipAddress.getText(), Integer.parseInt(port.getText()));
                    client = new Client(socket, ClientGUI.this);
                    InetAddress inetAddress = socket.getInetAddress();
                    System.out.println("InetAddress: " + inetAddress);
                    String remoteIp = inetAddress.getHostAddress();
                    System.out.println("Remote IP: " + remoteIp);
                    System.out.println("LocalPort:" + socket.getLocalPort());
                } catch (IOException err) {
                    textAreaChat.setText("Не удалось соединиться с сервером");
                    return;
                }
                if (client.connectToServer(clientName.getText(), pswd)) {
                    System.out.println("Login Successful");
                    name = clientName.getText();
                    setTitle(clientName.getText() + " window");
                    textAreaChat.append("-----You're logged to chat" + "\n");
                    textAreaChat.setText(loadChat);
                    client.listenForMessage();
                }else{
                    changeFormOnBreakConnection();
                }
            }
        });
    }

    private void createExitListener() {
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.disconnectFromServer();
                changeFormOnBreakConnection();
            }
        });
    }

    private void createSendListeners() {
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFunction();
            }
        });

        textSendField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFunction();
            }
        });
    }

    private void sendFunction() {
        client.sendMessage(name,textSendField.getText());
        textAreaChat.append(textSendField.getText() + "\n");
        textSendField.setText("");
    }

    public void changeFormOnBreakConnection(){
        changeFieldsVisibility(false);
        textAreaChat.setEnabled(false);
    }

    @Override
    public void getMessage(String message) {
        textAreaChat.append(message);
    }

    @Override
    public void changeFieldsVisibility(boolean status) {
        Arrays.stream(panelLogin.getComponents()).sequential().forEach(c->c.setEnabled(!status));
        btnExit.setEnabled(status);
        textSendField.setEnabled(status);
        btnSend.setEnabled(status);
        textAreaChat.setEnabled(status);
    }

    @Override
    public void chatLoad(String loadedChat) {
        loadChat = loadedChat;
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            client.disconnectFromServer();
            System.exit(0);
        }
    }
}
