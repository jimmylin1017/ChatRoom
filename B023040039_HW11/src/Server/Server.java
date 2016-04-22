package Server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;


public class Server  
{
	public HashMap<String,ArrayList<String>> groupmap=new HashMap<String,ArrayList<String>>();;
	public HashMap<String,PrintStream> printmap=new HashMap<String,PrintStream>();
	public ServerSocket skt;
	public static void main(String[] args)
	{
		
		Scanner keyboard=new Scanner(System.in);
		System.out.print("Port:");
		int port=keyboard.nextInt();
		
		keyboard.close();
		new Server(port).start();
		
	}
	public Server(int port)
	{
		try
		{
			skt=new ServerSocket(port);
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
				Socket clinetsokcket=skt.accept();	
				Thread t=new Thread(new Process(clinetsokcket));
				t.start();
			
			}
		}
		catch (Exception e)
		{
		}
	}
	
	public class Process implements Runnable{
		public String name;
		public String groupname;
		public Socket skt;
		private  BufferedReader reader; 
		public Process(Socket sk)
		{
			skt=sk;
		}
		public Process(String name,String groupname,Socket st)
		{
			try
			{
				this.name=name;
				this.groupname=groupname;
				skt=st;
				
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			
		}
		@Override
		public void run() 
		{
			try{
				System.out.println("new one come in");	
			
				InputStreamReader isReader = new InputStreamReader(skt.getInputStream()); 
				reader = new BufferedReader(isReader);
				
				name=reader.readLine();
				System.out.println("user name:"+name);
				PrintStream writer = new PrintStream(skt.getOutputStream());  
				printmap.put(name,writer);
				choosegroup();
				
				String types=reader.readLine();
				int type=Integer.parseInt(types);
				
				if (type==2)
				{
					
					groupname=reader.readLine();
					groupmap.get(groupname).add(name);
					System.out.println("join group "+groupname);
					
				}
				else 
				{
					
					groupname=reader.readLine();
					ArrayList<String> namelist=new ArrayList<String>();
					
					namelist.add(name);
					groupmap.put(groupname,namelist);
					System.out.println("new group "+groupname);
				}	
				
				membershow();
			
				String message;
				while ((message = reader.readLine())!=null)
				{
					broadcast(name+":"+message);
				}
				if (message==null)
				{
					broadcast(name+"is exit");
				}
				
				groupmap.get(groupname).remove(name);
				memberbroadcast();
				ArrayList<String> namelist=groupmap.get(groupname);
				if (namelist.size()==0)
				{
					groupmap.remove(groupname);
					System.out.println(groupname+"is delete");
					
				}
				skt.close();
				
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			
		}
		public void choosegroup()
		{
			PrintStream writer = (PrintStream) printmap.get(name);
			Set<String> key = groupmap.keySet();  
	
			for (String string : key) 
			{  
				ArrayList<String> namelist=groupmap.get(string);
				
				writer.println("Groupname:"+string);
				writer.flush();	
				writer.println("member:");
				writer.flush();
				for(int i=0;i<namelist.size();i++)
				{
					writer.println("    "+namelist.get(i));
					writer.flush();	
				}
			    
			}  

		}
		public void membershow()
		{
			PrintStream writer = (PrintStream) printmap.get(name);
			ArrayList<String> namelist=groupmap.get(groupname);
			
			writer.println("member:");
			writer.flush();
			for(int i=0;i<namelist.size();i++)
			{
				writer.println("    "+namelist.get(i));
				writer.flush();	
			}
		}
		public void memberbroadcast()
		{
			try
			{
				ArrayList<String> namelist=groupmap.get(groupname);
				for(int i=0;i<namelist.size();i++)
				{
					PrintStream writer = (PrintStream) printmap.get(namelist.get(i));
					
					writer.println("member:"+namelist.size());
					writer.flush();	
					
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			
		}
		public void broadcast(String message)
		{
			try{
				ArrayList<String> namelist=groupmap.get(groupname);
				for(int i=0;i<namelist.size();i++)
				{
					PrintStream writer = (PrintStream) printmap.get(namelist.get(i));
					
					writer.println(message);
					writer.flush();	
					
					
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		
	}

}
