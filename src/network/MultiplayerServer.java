package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

import ui.GameApp;

public class MultiplayerServer extends Server {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	public MultiplayerServer() throws IOException, InterruptedException {
		super();

		this.serverPort = 12345;
		serverIPAddress = InetAddress.getLocalHost().getHostAddress();
		
		serverSocket = new ServerSocket(serverPort);
		
		initServerThread();
	}


	private void initServerThread() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					searchForConnection();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					messageSender.put(Message.USERNAME, thisPlayer.getUserName());
					messageSender.put(Message.IS_READY, String.valueOf(thisPlayer.isReady()));
					messageSender.flush();
					
					listenAndProcessMessages();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(GameApp.getInstance(),   // TODO remove this
							"Connection with the client is lost.", 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	
	public void searchForConnection() throws IOException {
		clientSocket = serverSocket.accept();
		messageSender = new MessageSender(clientSocket.getOutputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	


	
}
