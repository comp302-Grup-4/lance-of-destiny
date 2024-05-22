package network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
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
					
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
					objectStream.writeObject(game.getAnimator().getBarrierGrid());
					objectStream.close();
					
					String barrierGridString = Base64.getEncoder().encodeToString(byteStream.toByteArray());
					messageSender.put(Message.BARRIER_GRID, barrierGridString);
					
					messageSender.flush();
					
					listenAndProcessMessages();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(GameApp.getInstance(),   // TODO remove this
							"Connection with the client is lost.", 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
					
					notifyObserver(Message.USERNAME, "Waiting for connection...");
					notifyObserver(Message.IS_READY, false);
					
					executor.submit(this);
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
