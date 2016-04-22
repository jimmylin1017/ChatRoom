package UI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class clientConnect extends JFrame {
	private final int WIDTH = 220;
	private final int HEIGHT = 220;
	private JFrame loginwindow;
	private JTextField ip;
	private JTextField port;
	private JLabel labelip;
	private JLabel labelport;
	private JButton login;
	private client newclient;
	private set setdatawindow;
	private Font font1 = new Font(Font.DIALOG, Font.BOLD, 20);
	
	public clientConnect() {
		loginwindow = new JFrame("Login");
		ip = new JTextField("",17);
		port = new JTextField("",17);
		labelip = new JLabel("ip");
		labelip.setFont(font1);
		labelport = new JLabel("port");
		labelport.setFont(font1);
		login = new JButton("connect");
		loginwindow.setLayout(new FlowLayout());
		loginwindow.getContentPane().add(labelip);
		loginwindow.getContentPane().add(ip);
		loginwindow.getContentPane().add(labelport);
		loginwindow.getContentPane().add(port);
		loginwindow.getContentPane().add(login);
		loginwindow.setSize(WIDTH, HEIGHT);
		loginwindow.setVisible(true);
		loginwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Socket clientsocket = null;
				try {
					clientsocket = new Socket(ip.getText(),Integer.valueOf(port.getText()));
					newclient = new client(clientsocket);
					setdatawindow=new set(newclient);
					loginwindow.dispose();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null,"connect failed");
				} catch (UnknownHostException e1) {
					JOptionPane.showMessageDialog(null,"connect failed");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null,"connect failed");
				}
			}
		});
	}

	public static void main(String[] args) {
		clientConnect login = new clientConnect();
	}

}

