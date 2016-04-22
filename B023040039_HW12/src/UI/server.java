package UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import UI.server.serverthread;

public class server {
	public HashMap<String, ArrayList<String>> group = new HashMap<String, ArrayList<String>>();
	public HashMap<String, PrintStream> member = new HashMap<String, PrintStream>();
	public ServerSocket serversocket;

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Port:");
		int port = keyboard.nextInt();
		server chatroom = new server(port);
		chatroom.getmember();
		keyboard.close();
	}

	public server(int port) {
		try {
			serversocket = new ServerSocket(port);
			System.out.println("Open port " + port + " successfully");
		} catch (Exception e) {
			System.out.println("Open server failed");
		}
	}

	public void broadcast(String message, String groupname) {
		try {
			ArrayList<String> namelist = group.get(groupname);
			for (int i = 0; i < namelist.size(); i++) {
				PrintStream writer = member.get(namelist.get(i));
				writer.println(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getmember() {
		try {
			while (true) {
				String groupname = "";
				String grouplist = "";
				Socket clinetsocket = serversocket.accept();
				PrintStream write = new PrintStream(
						clinetsocket.getOutputStream());
				for (Entry<String, ArrayList<String>> entry : group.entrySet()) {
					grouplist += (entry.getKey() + " ");
				}
				write.println(grouplist);
				BufferedReader read = new BufferedReader(new InputStreamReader(
						clinetsocket.getInputStream()));
				Scanner keyboard = new Scanner(System.in);
				String username = read.readLine();
				System.out.println(username + " comes in");
				String decision = read.readLine();
				groupname = read.readLine();
				member.put(username, write);
				if (Integer.parseInt(decision) == 2) {
					if (group.containsKey(groupname)) {
						group.get(groupname).add(username);
						System.out.println(username + " Join group \""+ groupname + "\" successed!");
					} else {
						ArrayList<String> memberlist = new ArrayList<String>();
						memberlist.add(username);
						group.put(groupname, memberlist);
						System.out.println("Create group \"" + groupname+ "\" successed!");
						System.out.println(username + " Join group \""+ groupname + "\" successed!");
					}
				} else {
					if (!group.containsKey(groupname)) {
						ArrayList<String> memberlist = new ArrayList<String>();
						memberlist.add(username);
						group.put(groupname, memberlist);
						System.out.println("Create group \"" + groupname+ "\" successed!");
						System.out.println(username + " Join group \""+ groupname + "\" successed!");
					} else {
						group.get(groupname).add(username);
						System.out.println(username + " Join group \""+ groupname + "\" successed!");
					}
				}
				String memberlist = "";
				ArrayList<String> tempgroup = group.get(groupname);
				for (int i = 0; i < tempgroup.size(); i++) {
					memberlist += (tempgroup.get(i) + " ");
				}
				broadcast(username + " Join this group", groupname);
				broadcast("CURRENT USERS:" + memberlist, groupname);
				write.println(memberlist);
				Thread t = new Thread(new serverthread(username, groupname,clinetsocket));
				t.start();
				keyboard.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public class serverthread extends Thread {
		public String username;
		public String groupname;
		public Socket clientsocket;
		private BufferedReader read;

		public serverthread(String username, String groupname,Socket clientsocket) {
			try {
				this.username = username;
				this.groupname = groupname;
				this.clientsocket = clientsocket;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		@Override
		public void run() {
			try {
				String message;
				read = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
				while ((message = read.readLine()) != null) {
					
					broadcast(message);
				}
				if (message == null) {
					group.get(groupname).remove(username);
					member.remove(username);
					broadcast(username + " leaves this room");
					String memberlist = "";
					ArrayList<String> tempgroup = group.get(groupname);
					for (int i = 0; i < tempgroup.size(); i++) {
						memberlist += (tempgroup.get(i) + " ");
					}
					broadcast("CURRENT USERS:" + memberlist);
					System.out.println(username + " leaves " + groupname + "\n");
					if (group.get(groupname).size() == 0) {
						group.remove(groupname);
						System.out.println("group: " + groupname+ " was removed\n");
					}
				}
				clientsocket.close();
			} catch (Exception e) {
				group.get(groupname).remove(username);
				member.remove(username);
				broadcast(username + " leaves this room");
				String memberlist = "";
				ArrayList<String> tempgroup = group.get(groupname);
				for (int i = 0; i < tempgroup.size(); i++) {
					memberlist += (tempgroup.get(i) + " ");
				}
				broadcast("CURRENT USERS:" + memberlist);
				System.out.println(username + " leaves " + groupname + "\n");
				if (group.get(groupname).size() == 0) {
					group.remove(groupname);
					System.out.println("group: " + groupname + " was removed\n");
				}
			}
		}

		public void broadcast(String message) {
			try {
				ArrayList<String> namelist = group.get(groupname);
				for (int i = 0; i < namelist.size(); i++) {
					PrintStream writer = member.get(namelist.get(i));
					writer.println(username + ":" + message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}