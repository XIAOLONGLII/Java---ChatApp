package cafe;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class ChatClient implements Runnable

{
	private Socket socket = null; 
	BufferedReader console = null; 
	private DataOutputStream streamOut = null; 
	private ChatClientThread client = null; 
	private boolean done = false; 
	private String line =" ";
	private Thread thread = null; 
	
	public ChatClient(String serverName, int serverPort)
	{
		try
		{
	socket = new Socket(serverName, serverPort);
	System.out.println("Got connected to socket: " +socket + "on port" + serverPort);
	start(); 
	String line =""; 
	while(!line.equalsIgnoreCase("bye"))
	{
	line = console.readLine();
	streamOut.writeUTF(line);
	streamOut.flush();		
		
	}
		}
	catch(UnknownHostException uhe)
	{
	System.out.println("Unknown host:  OOPS!"  + uhe.getMessage());
	
		
	}catch(IOException ioe)
	{
	System.out.println("IO problem client: " + ioe.getMessage()); 
	
	}
	
}
		
		
		
	
	public void start() throws IOException
	{
	console = new BufferedReader(new InputStreamReader(System.in));
	streamOut= new DataOutputStream(socket.getOutputStream());
	
	if(thread ==null)
		{
	client = new ChatClientThread(this, socket);
	thread = new Thread(this);
	thread.start();
	
		}
		
	}
	
	public void stop() 
	{
	done = true; 
	if(thread!=null)
		{
	thread= null; 	
		
		}
	try
	{
	if(console!=null) console.close();
	if(streamOut!=null)streamOut.close();
	if(client!=null)client = null;
	if(socket!=null)socket.close();
	
		
	}catch(IOException ioe)
	{
		System.out.println("Error closing..." + ioe.getMessage());
		
	}
	
	}
	
	public void run()
	{
		 
	while((thread!=null) && (!line.equalsIgnoreCase("bye")))
	{
	try
	{
		line = console.readLine();
		streamOut.writeUTF(line);
		streamOut.flush();
		
		
	}catch(IOException ioe)
	{
	System.out.println("Error sending the message" + ioe.getMessage());	
		
	}
		
	}
		
		
	}
	public void handle(String msg)
	{
		if (msg.equals("bye"))
		{  System.out.println("Good bye. Press RETURN to exit ...");
		stop();
		
		}
		else
			System.out.println(msg);
	
	}

	public static void main(String[] args)
	{
	ChatClient client =null; 
	
	if(args.length !=2)
	{
	System.out.println("To chat specify the servername and the port");
	
	}
	else
	{
	client = new ChatClient(args[0], Integer.parseInt(args[1]));	
	}
		
		
	}
	

	
	
}
