package UI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class client extends JFrame {
	
	private final int WIDTH = 500;
	private final int HEIGHT = 400;
	public JFrame chatroom;
	private ClientThread clientthread;
	private JTextArea messagebox;
	private JTextField input;
	private JButton enter;
	private Socket clientsocket;
	private Font font1 = new Font(Font.DIALOG, Font.PLAIN, 20);
	private JScrollPane vertical;

	public client(Socket clientsocket) {
		this.clientsocket = clientsocket;
		chatroom = new JFrame("chatroom");
		messagebox = new JTextArea(10, 25);
		messagebox.setFont(font1);
		vertical = new JScrollPane(messagebox);
		vertical.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		enter = new JButton("Enter");
		input = new JTextField(30);
		chatroom.getContentPane().add(vertical);
		chatroom.getContentPane().add(input);
		chatroom.getContentPane().add(enter);
		chatroom.setSize(WIDTH, HEIGHT);
		chatroom.setVisible(false);
		chatroom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatroom.setLayout(new FlowLayout());
		input.addKeyListener(new Enter_press());
		enter.addActionListener(new Enter_click());
	}

	public void getgroup(JTextArea groupbox) {
		clientthread = new ClientThread(clientsocket);
		String grouplist = clientthread.read();
		if (grouplist.equals(""))
			groupbox.append("No groups in the server");
		else
			groupbox.append("There are groups: " + grouplist);
		starttalk();
	}

	public void setdata(String decision, String username, String groupname) {
		clientthread.send(username);
		clientthread.send(decision);
		clientthread.send(groupname);
	}

	public void starttalk() {
		clientthread.start();
	}

	public class Enter_click implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!input.getText().equals("")) {
				if (input.getText().equals("EXIT"))
					clientthread.stopthread();
				else
					clientthread.send(input.getText());
				input.setText("");
			}
		}
	}

	private class Enter_press implements KeyListener {
		public void keyPressed(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {// «ö¤UEnter
			if (!input.getText().equals("")) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!input.getText().equals("")) {
						if (input.getText().equals("EXIT"))
							clientthread.stopthread();
						else
							clientthread.send(input.getText());
						input.setText("");
					}
				}
			}
		}
	}

	public class ClientThread extends Thread {
		private BufferedReader read;
		private PrintStream write;
		public String message;

		public ClientThread(Socket clientsocket) {
			try {
				read = new BufferedReader(new InputStreamReader(
						clientsocket.getInputStream()));
				write = new PrintStream(clientsocket.getOutputStream());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			try {
				while ((message = read.readLine()) != null) {
					messagebox.append(message + "\n");
				}
				clientsocket.close();
			} catch (IOException e) {
				messagebox.append("Disconnecetion\n");
			}
		}

		public void stopthread() {
			try {
				clientsocket.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

		public void send(String data) {
			write.println(data);
		}

		public String read() {
			try {
				return read.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}

