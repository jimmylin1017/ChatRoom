/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
*/
import java.util.*;
import java.io.*;
import java.net.*;

public class Server
{
	//public HashMap<String,ArrayList<String>> groupMap = new HashMap<String, ArrayList<String>>();
	public ArrayList<String> memberList = new ArrayList<String>();
	public HashMap<String,PrintStream> printStreamMap = new HashMap<String, PrintStream>();
	public ArrayList<String> articleList = new ArrayList<String>();
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
		//public String groupName;
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
				memberList.add(name);
				
				PrintStream outputToClinet = new PrintStream(clientSkt.getOutputStream());
				printStreamMap.put(name, outputToClinet);
				

				/*

				command : @MEM, @RENAME, @SHOWPOST, @POST, @GETPOST

				*/
				broadcast(name+ " enters the Lobby");
				
				String message;

				while((message = clientReader.readLine()) != null)
				{
					String commandArr[] = message.split(" ", 2);
					if(commandArr[0].toUpperCase().equals("@MEM"))
					{
						showMember();
					}
					else if(commandArr[0].toUpperCase().equals("@RENAME"))
					{
						name = changeName(name, commandArr[1]);
					}
					else if(commandArr[0].toUpperCase().equals("@SHOWPOST"))
					{
						showAllArticle();
					}
					else if(commandArr[0].toUpperCase().equals("@POST"))
					{
						String articleArr[] = commandArr[1].split(" ", 2);
						articleList.add(articleArr[0]);
						try
						{
							new File("article/" + articleArr[0]).createNewFile();
							FileWriter fw = new FileWriter("article/" + articleArr[0]);
							int lines = Integer.parseInt(articleArr[1]);
							System.out.println("lines : " + lines);
							for(int i=0;i<lines;i++)
							{
								message = clientReader.readLine();
								fw.write(message + "\n");
							}
							fw.close();
							System.out.println("get post : " + articleArr[0]);
						}
						catch(Exception e)
						{
							System.out.println(e.getMessage());
						}
					}
					else if(commandArr[0].toUpperCase().equals("@GETPOST"))
					{
						showArticleContent(commandArr[1]);
					}
					else
					{
						broadcast(name + " : " + message);
					}
				}

				if(message == null)
				{
					broadcast(name + " exits the Lobby");
				}

				memberList.remove(name);
				printStreamMap.remove(name);
				
				clientSkt.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}

		public void showMember()
		{
			PrintStream outputToClinet = (PrintStream) printStreamMap.get(name);
			
			outputToClinet.println("member:");
			outputToClinet.flush();

			for(int i = 0 ; i < memberList.size() ; i++)
			{
				outputToClinet.println(i + " : " + memberList.get(i));
				outputToClinet.flush();	
			}
		}

		public String changeName(String oldName, String newName)
		{
			PrintStream outputToClinet = (PrintStream) printStreamMap.get(oldName);

			memberList.remove(oldName);
			printStreamMap.remove(oldName);

			memberList.add(newName);
			printStreamMap.put(newName, outputToClinet);
			
			broadcast(oldName + " change name to " + newName);

			return newName;
		}

		public void showArticleContent(String articleName)
		{
			PrintStream outputToClinet = (PrintStream) printStreamMap.get(name);

			try
			{
				FileReader fr = new FileReader("article/" + articleName);
				BufferedReader frbr = new BufferedReader(fr);
				
				String content;
				while((content = frbr.readLine()) != null)
				{
					outputToClinet.println("# " + content);
					outputToClinet.flush();	
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}

		public void showAllArticle()
		{
			PrintStream outputToClinet = (PrintStream) printStreamMap.get(name);
			
			//outputToClinet.println("article:");
			//outputToClinet.flush();

			for(int i = 0 ; i < articleList.size() ; i++)
			{
				outputToClinet.println("#@! " + i + " : " + articleList.get(i));
				outputToClinet.flush();
			}
		}


		public void broadcast(String message)
		{
			try
			{
				// get member from group
				//ArrayList<String> namelist = groupMap.get(groupName);
				
				// output to all member
				for(int i = 0 ; i < memberList.size() ; i++)
				{
					// get member output stream
					PrintStream outputToClinet = (PrintStream) printStreamMap.get(memberList.get(i));
					
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
