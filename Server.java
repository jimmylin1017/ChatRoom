import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import java.io.*;


public class Server
{
	public HashMap<String,ArrayList<String>> groupMap = new HashMap<String, ArrayList<String>>();
	public HashMap<String,PrintStream> printStreamMap = new HashMap<String, PrintStream>();
	public ServerSocket serverSkt;

	public static void main(String[] args)
	{
		
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Port:");
		int port = keyboard.nextInt();
		
		keyboard.close();

		// run server
		new Server(port).start();
		
	}

	public Server(int port)
	{
		try
		{
			serverSkt = new ServerSocket(port);
		}
		catch(Exception e)
		{
			
			System.out.println(e.getMessage());
		}
	}

	public void start()
	{
		try{
			while(true)
			{
				System.out.println("wait for connectiong");
				Socket clientSkt = serverSkt.accept();
				Thread t = new Thread(new clientHandler(clientSkt));
				t.start();
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public class clientHandler implements Runnable
	{
		public String name;
		public String groupName;
		public Socket clientSkt;
		private BufferedReader clientReader;
		
		public clientHandler(Socket skt)
		{
			clientSkt = skt;
		}

		@Override
		public void run()
		{
			try
			{
				System.out.println("new one come in");	
			
				InputStreamReader isReader = new InputStreamReader(clientSkt.getInputStream()); 
				clientReader = new BufferedReader(isReader);
				
				// read name from client
				name = clientReader.readLine();
				System.out.println("user name:" + name);
				
				PrintStream outputToClinet = new PrintStream(clientSkt.getOutputStream());
				printStreamMap.put(name, outputToClinet);
				
				showGroup();
				
				// 1 - add group, 2 - choose group
				String types = clientReader.readLine();
				int type = Integer.parseInt(types);

				if(type == 2)
				{
					// join group
					groupName = clientReader.readLine();
					
					if(groupMap.get(groupName) != null)
					{
						// add name to groupMap's ArrayList by groupName
						groupMap.get(groupName).add(name);
						
						System.out.println("join group " + groupName);
					}
					else
					{
						// creat new group
						ArrayList<String> nameList = new ArrayList<String>();
						
						nameList.add(name);
						groupMap.put(groupName, nameList);
						System.out.println("Because group name is error, creat new group " + groupName);
					}
				}
				else if(type == 1)
				{
					// creat new group
					groupName = clientReader.readLine();
					ArrayList<String> nameList = new ArrayList<String>();

					nameList.add(name);

					groupMap.put(groupName, nameList);

					System.out.println("new group " + groupName);
				}
				
				showGroupMember();
			
				String message;

				while((message = clientReader.readLine()) != null)
				{
					String command[] = message.split(" ", 2);
					if(command[0].toUpperCase().equals("@MEM"))
					{
						showGroupMember();
					}
					else if(command[0].toUpperCase().equals("@RENAME"))
					{
						groupMap.get(groupName).remove(name);
						printStreamMap.remove(name);
						name = command[1];
						groupMap.get(groupName).add(name);
						printStreamMap.put(name, outputToClinet);
					}
					else if(command[0].toUpperCase().equals("@FILE"))
					{
						//BufferedInputStream inputStream = new BufferedInputStream(clientSkt.getInputStream()); 
						BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("file/" + command[1])); 
						System.out.println("waiting file....");
						int counter = Integer.parseInt(clientReader.readLine());
						System.out.println("counter : " + counter);
						int readin;
						while(counter > 0)
						{
							readin = clientReader.read();
							outputStream.write(readin);
							System.out.println(readin);
							counter--;
                		}

                		outputStream.flush();
		                outputStream.close();                
		                //inputStream.close(); 
                		System.out.println("get file!");
					}
					else
					{
						broadcast(name + ":" + message);
					}
				}

				if(message == null)
				{
					broadcast(name + "is exit");
				}
				
				// remove name from group
				groupMap.get(groupName).remove(name);

				memberNumBroadcast();

				// if group member is zero, remove group
				ArrayList<String> namelist = groupMap.get(groupName);
				
				if(namelist.size() == 0)
				{
					groupMap.remove(groupName);
					System.out.println("group : " + groupName + " is delete!");
				}
				
				clientSkt.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}

		public void showGroup()
		{
			// get name output stream
			PrintStream outputToClinet = (PrintStream) printStreamMap.get(name);
			
			// get all name from gruopMap
			Set<String> key = groupMap.keySet();

			// print group data
			for (String string : key) 
			{
				// get all group from gruopMap
				ArrayList<String> nameList = groupMap.get(string);
				
				outputToClinet.println("Groupname:"+string);
				outputToClinet.flush();
				outputToClinet.println("member:");
				outputToClinet.flush();

				for(int i = 0 ; i < nameList.size() ; i++)
				{
					// print all member
					outputToClinet.println(i + " : " + nameList.get(i));
					outputToClinet.flush();
				}
			}
		}

		public void showGroupMember()
		{
			PrintStream outputToClinet = (PrintStream) printStreamMap.get(name);
			outputToClinet.println("join in group : " + groupName);

			// get group member
			ArrayList<String> nameList = groupMap.get(groupName);
			
			outputToClinet.println("member:");
			outputToClinet.flush();

			for(int i = 0 ; i < nameList.size() ; i++)
			{
				outputToClinet.println(i + " : " + nameList.get(i));
				outputToClinet.flush();	
			}
		}

		public void memberNumBroadcast()
		{
			try
			{
				// get member from group
				ArrayList<String> namelist = groupMap.get(groupName);
				
				for(int i = 0 ; i < namelist.size() ; i++)
				{
					// get member output stream
					PrintStream outputToClinet = (PrintStream) printStreamMap.get(namelist.get(i));
					
					outputToClinet.println("member : " + namelist.size());
					outputToClinet.flush();	
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			
		}

		public void broadcast(String message)
		{
			try
			{
				// get member from group
				ArrayList<String> namelist = groupMap.get(groupName);
				
				// output to all member
				for(int i = 0 ; i < namelist.size() ; i++)
				{
					// get member output stream
					PrintStream outputToClinet = (PrintStream) printStreamMap.get(namelist.get(i));
					
					outputToClinet.println(message);
					outputToClinet.flush();	
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		
	}

}
