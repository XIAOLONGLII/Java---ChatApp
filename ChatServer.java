package cafe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable
{

	private ServerSocket server = null; 
	private ChatServerThread clients [] = new ChatServerThread[5];
	private Thread thread = null; 
	private int clientCount= 0; 
	
	private boolean done = true; 
	
	public synchronized void handle(int ID, String input)
	{
	String privMesg = "private message";
	
	if(input.startsWith(privMesg))
	{
	int ID_SendTo = Integer.parseInt(input.substring(privMesg.length() + 5));
	String msg = input.substring(privMesg.length() + 6);
	
	if(findClient(ID_SendTo) != -1)
	{
		clients[findClient(ID_SendTo)] .send(msg + "from" + ID + ":" + input);
		
	}
		
		
	}
	else
	for(int i =0; i<clientCount; i++)
	{
	clients[i].send("USER: " + ID + " said " + input);	
	
	}
		
		if(input.equalsIgnoreCase("bye"))
		{
		remove(ID); 	
			
		}
		
		
		
	}
	
	public synchronized void remove(int ID)
	{
	int pos = findClient(ID);
	if(pos >=0)
	{
	ChatServerThread toTerminate= clients[pos];
	System.out.println("Removing client thread " + ID + "at" + pos);
	
	if(pos <clientCount -1)
	{
	for(int i= pos+1; i<clientCount;i++ )
	{
	clients[i-1] = clients[i];
	
		
	}
	clientCount--; 
	}
	try
	{
	toTerminate.close();
	
		
	}catch(IOException ioe)
	{
	System.out.println("ERROR closing the thread " + ioe.getMessage());
		
	}
		
	}
	}
		
		
		
	
	
	
	private int findClient(int ID) 
	{
	for(int i = 0; i<clientCount; i++)
	{
	if(clients[i].getID() == ID)
	{
		return i;
		
	}
		
		
	}
	return -1;
		
	}

	
	private synchronized void addThread(Socket socket)
	{
	if(clientCount <clients.length)
	{
	clients[clientCount] = new ChatServerThread(this, socket);
	
	try
	{
	clients[clientCount].open(); 
	clients[clientCount].start();
	clientCount++;
		
		
	}catch(IOException ioe)
	{
	System.out.println("OOPS! tried to add thread client " + ioe.getMessage());	
	}
		
	}
		
		
	}
	@Override
	public void run() 
	{
		while(thread!= null)
		{
		try{
		System.out.println("Waiting for a client... inside run");
		addThread(server.accept());
			
			
		}catch(IOException ioe)
		{
		System.out.println("OOPSY " + ioe.getMessage());	
			
		}
			
		}
		
		
	}
	
	
	public ChatServer(int port)
	{
	try
	{
	System.out.println("Binding to port " + port + "..wait!!!");
	server = new ServerSocket(port);
	start();
	
		
	}catch(IOException ioe)
	{
	System.out.println("OOPS: " + ioe.getMessage());	
		
	}
		
		
		
	}
	public void start()
	{
	if(thread == null)
	{
	thread = new Thread(this);
	thread.start();
	
		
	}
		
	}
	public void stop()
	{
	if(thread!= null)
	{
		thread = null; 
		done = true; 
	}
		
	}
	
	public static void main(String[] args)
	{
		
	ChatServer server = null; 
	if(args.length !=1)
	{
	System.out.println("To run a chat server you need to specify a Port");
	
		
	}else 
	{
	server = new ChatServer(Integer.parseInt(args[0]));	
		
	}
		
	}
	
	
	
	
}


