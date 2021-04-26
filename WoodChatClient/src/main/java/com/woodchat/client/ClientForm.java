package com.woodchat.client;


import com.woodchat.server.message.Message;
import com.woodchat.server.message.User;
import com.woodchat.server.network.SocketConnection;
import com.woodchat.server.network.ConnectionObserver;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientForm extends JFrame implements ActionListener, ConnectionObserver {
    private static final String IP_ADDRESS = "192.168.1.33";
    private static final int PORT = 8080;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientForm();
            }
        });
    }


    private final JTextField fieldName = new JTextField("Name");
    private final JTextField fieldIP = new JTextField("192.168.1.33");
    private final JTextField fieldPort = new JTextField("8080");
    private final JTextField fieldIn = new JTextField();
    private final JTextField rColor = new JTextField("255");
    private final JTextField gColor = new JTextField("255");
    private final JTextField bColor = new JTextField("0");
    private final JButton enterSettings = new JButton("Enter");
    private final JLabel resultConnection = new JLabel();
    private final JPanel settingsPanel = new JPanel();
    private final JPanel usersPanel = new JPanel();
    private final JPanel messageInputPanel = new JPanel();
    private final JPanel messagePanel = new JPanel();
    private final JScrollPane messageScrollPanel = new JScrollPane(messagePanel);
    private final Color c1 = new Color(242, 212, 148);
    private final Color c2 = new Color(182, 220, 135);
    //private final ImageIcon iconChange = new ImageIcon("C:\\Users\\kori1I\\IdeaProjects\\WoodChat\\WoodChatClient\\Change.png");
    //private final ImageIcon iconDelete = new ImageIcon("C:\\Users\\kori1I\\IdeaProjects\\WoodChat\\WoodChatClient\\delete.png");
    private SocketConnection socketConnection;
    private User clientUser = new User();

    private ClientForm() {
        JFrame mainWindow = new JFrame("WoodChat");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gbl = new GridBagLayout();
        mainWindow.setLayout(gbl);
        mainWindow.setSize(WIDTH, HEIGHT);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setAlwaysOnTop(true);
        usersPanel.setBackground(new Color(15, 100, 100));
        messageScrollPanel.setBackground(c1);
        messageInputPanel.setBackground(c2);
        messageInputPanel.add(fieldIn);
        settingsPanel.setBackground(new Color(100, 100, 100));
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.add(new JLabel("Enter server IP"));
        settingsPanel.add(fieldIP);
        settingsPanel.add(new JLabel("Enter server PORT"));
        settingsPanel.add(fieldPort);
        settingsPanel.add(new JLabel("Enter user name"));
        settingsPanel.add(fieldName);
        settingsPanel.add(new Label("Enter color"));
        settingsPanel.add(rColor);
        settingsPanel.add(gColor);
        settingsPanel.add(bColor);
        settingsPanel.add(enterSettings);
        settingsPanel.add(resultConnection);

        enterSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (!fieldIP.getText().equals("") && !fieldPort.getText().equals("") && !fieldName.getText().equals("") && !rColor.getText().equals("") && !gColor.getText().equals("") && !bColor.getText().equals("")) {
                    int r = Integer.parseInt(rColor.getText());
                    int g = Integer.parseInt(gColor.getText());
                    int b = Integer.parseInt(bColor.getText());
                    createConnection(fieldIP.getText(), fieldPort.getText(), fieldName.getText(), new Color(r, g, b));
                } else {
                    resultConnection.setText("Fill in all the fields");
                    settingsPanel.revalidate();
                }

            }
        });

        fieldIn.setBackground(c1);
        fieldIn.setMinimumSize(new Dimension(500, 100));
        fieldIn.setPreferredSize(new Dimension(500, 100));
        fieldIn.addActionListener(this);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.fill = GridBagConstraints.BOTH;
        gbcLeft.weightx = 0.15;
        gbcLeft.weighty = 0.95;
        gbcLeft.anchor = GridBagConstraints.CENTER;
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.fill = GridBagConstraints.BOTH;
        gbcCenter.weightx = 0.70;
        gbcCenter.weighty = 0.95;
        gbcCenter.anchor = GridBagConstraints.CENTER;
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.fill = GridBagConstraints.BOTH;
        gbcRight.weightx = 0.15;
        gbcRight.weighty = 0.95;
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.gridwidth = GridBagConstraints.REMAINDER;
        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.fill = GridBagConstraints.BOTH;
        gbcBottom.weightx = 1.0f;
        gbcBottom.weighty = 0.05;
        gbcBottom.anchor = GridBagConstraints.SOUTH;
        gbcBottom.gridwidth = GridBagConstraints.REMAINDER;

        gbl.setConstraints(settingsPanel, gbcLeft);
        mainWindow.add(settingsPanel);
        gbl.setConstraints(messageScrollPanel, gbcCenter);
        mainWindow.add(messageScrollPanel);
        gbl.setConstraints(usersPanel, gbcRight);
        mainWindow.add(usersPanel);
        gbl.setConstraints(messageInputPanel, gbcBottom);
        mainWindow.add(messageInputPanel);
        mainWindow.setVisible(true);
    }

    public void createConnection(String ip, String port, String userName, Color color) {
        try {
            socketConnection = new SocketConnection(this, IP_ADDRESS, PORT);
            clientUser.setUserName(userName);
            clientUser.setColor(color);
        } catch (IOException e) {
            resultConnection.setText("Connection failed");
            settingsPanel.revalidate();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //String user = fieldName.getText();
        String msg = fieldIn.getText();
        if (!msg.equals("")) {
            fieldIn.setText(null);
            Message message = new Message(msg, clientUser);
            socketConnection.sendMessage(message);
        }
    }

    @Override
    public void onConnectionReady(SocketConnection socketConnection) {
        socketConnection.sendMessage(new Message("User:" + clientUser.getUserName(), new User("System", clientUser.getColor())));
    }

    @Override
    public void onReceiveMessage(SocketConnection socketConnection, Message message) {
        System.out.println(message.getText());
        if (message.getUser().getUserName().equals("System")) {
            if (message.getText().contains("User exists")) {
                if (message.getText().contains(clientUser.getUserName())) {
                    resultConnection.setText("User exists");
                    settingsPanel.revalidate();
                    socketConnection.disconnect();
                } else {
                    resultConnection.setText("");
                    settingsPanel.revalidate();
                    printMessage(message);
                }
            } else {
                resultConnection.setText("");
                settingsPanel.revalidate();
                printMessage(message);
            }
        } else {
            resultConnection.setText("");
            settingsPanel.revalidate();
            printMessage(message);
        }


    }

    @Override
    public void onDisconnect(SocketConnection socketConnection) {
        printMessage(new Message("SocketConnection close"));
    }

    @Override
    public void onExpression(SocketConnection socketConnection, Exception e) {
        printMessage(new Message("SocketConnection exception:" + e));
    }

    private synchronized void printMessage(Message message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                JPanel messageArea = new JPanel();
                messageArea.setLayout(new BorderLayout());
                messageArea.setBackground(message.getUser().getColor());
                messageArea.setPreferredSize(new Dimension(300, 100));
                messageArea.setBorder(new BevelBorder(1));
                JTextArea label = new JTextArea(message.getText());
                label.setLineWrap(true);
                label.setEditable(false);
                //JLabel label = new JLabel(message.getText());
                label.setBackground(message.getUser().getColor());
                JLabel user = new JLabel(message.getUser().getUserName());
                JLabel time = new JLabel(message.getTimeMessage().toString());

                /*
                JButton button2 = new JButton();
                button2.setIcon(iconDelete);
                button2.setPreferredSize(new Dimension(20, 20));
                button2.setBorderPainted(false);
                button2.setFocusPainted(false);
                button2.setBackground(new Color(255, 255, 255, 0));
                button2.setContentAreaFilled(false);
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(20, 20));
                button.setIcon(iconChange);

                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setBackground(new Color(255, 255, 255, 0));
                button.setContentAreaFilled(false);
                Box buttons = Box.createHorizontalBox();
                buttons.add(button);
                buttons.add(button2);
                messageArea.add(buttons, BorderLayout.EAST);*/
                messageArea.add(label, BorderLayout.CENTER);
                messageArea.add(user, BorderLayout.NORTH);
                messageArea.add(time, BorderLayout.SOUTH);
                messagePanel.add(messageArea);
                messageScrollPanel.revalidate();
            }
        });
    }

}
