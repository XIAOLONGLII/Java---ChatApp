package cafe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatGUI extends JFrame implements ActionListener, Runnable

{

	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private ChatClientThread client = null; 
	private JTextArea display = new JTextArea();
	private JTextField input = new JTextField();
	private JButton send = new JButton("Send"); 
	private JButton connect = new JButton("Connect"); 
	private JButton quit = new JButton("disconnect");
	private JButton sendPrivate = new JButton("Send Private");
	private String serverName = "localhost";
	private JTextField users = new JTextField();
	
	private int serverPort = 8081;
	
	private boolean done = true; 
	
	private String line = ""; 
	
	JPanel mainPanel = new JPanel(); 
	JPanel keys = new JPanel(); 
	JPanel south = new JPanel(); 
	
	
	
	
	public ChatGUI()
	{
	mainPanel.setLayout(new BorderLayout());
	keys.setLayout(new GridLayout(1,3));
	connect.setEnabled(true);
	keys.add(quit);
	keys.add(connect);
	keys.add(sendPrivate);
	
	Label title =new Label("Web Chat",Label.CENTER);
	title.setFont(new Font("Helvetica",Font.BOLD,14));
	mainPanel.add(title, BorderLayout.NORTH);
	
	south.setLayout(new BorderLayout());
	south.add("West", keys);
	south.add("Center", input);
	
	
	
	send.setEnabled(false);
	
	send.addActionListener(this);
	connect.addActionListener(this);
	quit.addActionListener(this);
	sendPrivate.addActionListener(this);
	
	display.setEditable(false);
	display.setBackground(Color.PINK);
	
	mainPanel.add(display,BorderLayout.CENTER);
	mainPanel.add(south, BorderLayout.SOUTH);
	
	
	south.add("East", send);
	
	add(mainPanel);

		
	}




	
	public void open()
	{
	try{
	streamOut = new DataOutputStream(socket.getOutputStream());
	streamIn = new DataInputStream(socket.getInputStream());
	new Thread(this).start();

	}catch(IOException ioe)
	{
	ioe.printStackTrace();	
		
	}
		
		
	}

	@Override
	public void run()
	{
	try {
		while(!done)
		{
		line = streamIn.readUTF();	
		displayOutput(line);
		}
	}catch(IOException ioe)
		{
		done = true; 
		displayOutput(ioe.getMessage());
			
		}
		
		
		
		}
	
	
		
	public void connect(String serverName, int serverPort)
	{
		
	done = false; 
	displayOutput("Connection time..");
	try
	{
	socket = new Socket(serverName, serverPort);
	displayOutput("Connected: " + socket);
	open(); 
	send.setEnabled(true);
	quit.setEnabled(true);
	connect.setEnabled(true);
	
	
	}catch(UnknownHostException uhe)
	{
	displayOutput(uhe.getMessage());	
		done=true; 
	}
		catch(IOException ioe)
	{
			
		displayOutput(ioe.getMessage());
		done=true; 
	}
		
	}

	public void disconnect()
	{
	done=true; 
	input.setText("disconnect");
	send(); 
	quit.setEnabled(false);
	connect.setEnabled(true);
	send.setEnabled(true);
		
	}

	public void send()
	{

	String msg= input.getText().trim();
	try
	{
	streamOut.writeUTF(msg);
	streamOut.flush();
	if(msg.equalsIgnoreCase("disconnect"))
		{
		quit.setEnabled(false);
		connect.setEnabled(false);
		send.setEnabled(false);
		close();
		
		
		}
	}catch(IOException ioe)

	{
	displayOutput("Problem sending.. " + ioe.getMessage());
	close(); 
		
	}
		
	}
		
		
		


	
	public void displayOutput(String msg)
	{
	display.append("\n" + msg + "\n-----");
	
	
		
	}
	
	public void close()
	{
	done = true; 
	try
	{
	if(streamOut!=null)
	{
		streamOut.close();
	}
		if(socket!=null)
		{
		socket.close();	
			
		}
		
	}catch(IOException ioe)
	{
	displayOutput("OOPS problem closing");
	client.close(); 
	client=null; 
		
	}
		
		
	}	
	
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
	String btnTxt = e.getActionCommand().trim(); 
	
	if(btnTxt.equalsIgnoreCase("connect"))
	{
	connect(serverName,serverPort);
	System.out.println("Connect button hit");
	
	}
	else if(e.getSource() == quit)
	{
		disconnect();
		System.out.println("quit button hit");
	
	}
	else if(e.getSource() == send )
	{
	send(); 
	System.out.println("send button hit");
		
	}
	
		
	}
	
	
}

