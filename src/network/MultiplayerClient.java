package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import ui.GameApp;

public class MultiplayerClient extends Server {
	Socket socket;
	
	public MultiplayerClient(String serverIP, int serverPort) throws UnknownHostException, IOException {
		super();
		
		this.serverIPAddress = serverIP;
		this.serverPort = serverPort;
		
		System.out.println("Client-in : Trying to connect to <<" + serverIP + ">> : <<" + serverPort + ">>.");
		
		socket = new Socket(serverIP, serverPort);
		
		System.out.println("Client-in : Connected to <<" + serverIP + ">> : <<" + serverPort + ">>.");
		
		messageSender = new MessageSender(socket.getOutputStream());
		
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		messageSender.put(Message.USERNAME, thisPlayer.getUserName());
		messageSender.flush();
		
		initServerThread();
		
	}

	private void initServerThread() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					listenAndProcessMessages();	
				} catch (NumberFormatException | IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(GameApp.getInstance(),   // TODO remove this
								"Connection with the server is lost.", 
								"Error", 
								JOptionPane.ERROR_MESSAGE);
				};
			}

			
			
		});
	}
	
	
}
