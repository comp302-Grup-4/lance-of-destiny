package ui;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import domain.MultiplayerGame;
import network.MultiplayerClient;

public class ConnectionScreen extends JPanel {
	private static final long serialVersionUID = 2331046925076603517L;
	
	JTextArea serverPortTextArea;
	JTextArea serverIPTextArea;
	
	public ConnectionScreen() {
		this.setLayout(new GridBagLayout());

		JButton hostButton = new JButton("Host a Game");
		
		JButton joinButton = new JButton("Join a Game");
		
		JLabel IPinstructionLabel = new JLabel("Enter the activeServer IP:");
		IPinstructionLabel.setVisible(false);
		
		serverIPTextArea = new JTextArea();
		serverIPTextArea.setVisible(false);
		
		JLabel portInstructionLabel = new JLabel("Enter the activeServer port:");
		portInstructionLabel.setVisible(false);
		
		serverPortTextArea = new JTextArea();
		serverPortTextArea.setVisible(false);
		
		JButton connectButton = new JButton("Connect");
		connectButton.setVisible(false);
		
		joinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				serverIPTextArea.setVisible(true);
				connectButton.setVisible(true);
				IPinstructionLabel.setVisible(true);
				portInstructionLabel.setVisible(true);
				serverPortTextArea.setVisible(true);
			}
		});
		
		hostButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				serverIPTextArea.setVisible(false);
				connectButton.setVisible(false);
				IPinstructionLabel.setVisible(false);
				portInstructionLabel.setVisible(false);
				serverPortTextArea.setVisible(false);

				MultiplayerGame multiplayer = new MultiplayerGame();
				GameApp.getInstance().setActiveGame(multiplayer);
				
				GameApp.getInstance().openBuildingScreen();
//				if (hostingThread.getState() == Thread.State.NEW)
//					hostingThread.start();
			}
		});
		
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				String IP = serverIPTextArea.getText();
				String portString = serverPortTextArea.getText();
				if (!portString.isBlank() && !IP.isBlank()) {
					int port = Integer.valueOf(serverPortTextArea.getText());
					
					try {
						MultiplayerClient client = new MultiplayerClient(IP, port);
						MultiplayerGame multiplayerGame = new MultiplayerGame();
						
						GameApp.getInstance().setActiveGame(multiplayerGame);
						client.registerObserver(multiplayerGame);
						
						GameApp.getInstance().setActiveServer(client);
						GameApp.getInstance().openClientWaitingScreen();
						
						
					} catch (IOException e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(ConnectionScreen.this, 
								"Connection could not be established.", 
								"Error", 
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(ConnectionScreen.this, 
							"The IP address or port is not valid.", 
							"Error", 
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(8, 1));
		
		contentPanel.add(hostButton);
		
		contentPanel.add(joinButton);
		contentPanel.add(IPinstructionLabel);
		contentPanel.add(serverIPTextArea);
		contentPanel.add(portInstructionLabel);
		contentPanel.add(serverPortTextArea);
		contentPanel.add(connectButton);
		
		this.add(contentPanel);
		
	}
	
}
