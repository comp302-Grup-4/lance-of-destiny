package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.MultiplayerGame;
import domain.Player;
import network.Message;
import network.MultiplayerObserver;
import network.MultiplayerServer;

public class HostWaitingScreen extends JPanel implements MultiplayerObserver {
	private static final long serialVersionUID = -1353861596312976187L;
	
	Player thisPlayer;
	Player otherPlayer;
	JLabel thisPlayerLabel, otherPlayerLabel;
	JButton hostGetReadyButton;
	JButton startGameButton;
	JLabel thisPlayerIsReadyLabel, otherPlayerIsReadyLabel;
	
	Font serifBoldFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	Font monospacedFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	Font serifNormalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
	
	JLabel hostIPLabel, hostPortLabel;
	MultiplayerGame game;
	
	public HostWaitingScreen() {
		this.game = (MultiplayerGame) GameApp.getInstance().getActiveGame();
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(3, 2));
		
		thisPlayer = new Player(GameApp.getInstance().getActivePlayerAccount());
		otherPlayer = new Player();
		otherPlayer.getPlayerAccount().setUserName("Waiting for connection...");
		game.setOtherPlayer(otherPlayer);
		
		thisPlayerLabel = new JLabel(thisPlayer.getUserName());
		innerPanel.add(thisPlayerLabel);
		
		otherPlayerLabel = new JLabel(otherPlayer.getUserName());
		innerPanel.add(otherPlayerLabel);

		thisPlayerIsReadyLabel = new JLabel("Not ready");
		innerPanel.add(thisPlayerIsReadyLabel);
		
		otherPlayerIsReadyLabel = new JLabel("Not ready");
		innerPanel.add(otherPlayerIsReadyLabel);
		
		hostGetReadyButton = new JButton("Get Ready");
		innerPanel.add(hostGetReadyButton);
		
		hostGetReadyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				thisPlayer.setReady(!thisPlayer.isReady());
				if (thisPlayer.isReady()) {
					hostGetReadyButton.setText("Unready");
				} else {
					hostGetReadyButton.setText("Get Ready");
				}
				
				GameApp.getInstance().getActiveServer().update(Message.IS_READY, thisPlayer.isReady());
				updateObjectsEnabledStatus();
			}
		});

		gbc.weighty = 0.3;
		gbc.gridy = 1;
		
		JPanel connectPanel = new JPanel();
		connectPanel.setLayout(new GridLayout(3, 1));

		JLabel connectInstructionLabel = new JLabel("Current activeServer information:");
		connectInstructionLabel.setFont(serifBoldFont);
		connectPanel.add(connectInstructionLabel);
		
		
		hostIPLabel = new JLabel();
		hostIPLabel.setFont(monospacedFont);
		connectPanel.add(hostIPLabel);
		
		hostPortLabel = new JLabel();
		hostPortLabel.setFont(monospacedFont);
		connectPanel.add(hostPortLabel);

		this.add(connectPanel, gbc);
		
		gbc.weighty = 0.3;
		gbc.gridy = 2;
		
		this.add(innerPanel, gbc);

		gbc.weighty = 0.3;
		gbc.gridy = 3;
		
		startGameButton = new JButton("Start Game");
		startGameButton.setEnabled(false);
		
		this.add(startGameButton, gbc);
		updateObjectsEnabledStatus();
		
		MultiplayerServer server;
		try {
			server = new MultiplayerServer();
			GameApp.getInstance().setActiveServer(server);
			server.registerObserver(this);
			server.registerObserver((MultiplayerGame) GameApp.getInstance().getActiveGame());
			
			hostIPLabel.setText("IP: " + server.getServerIPAddress());
			hostPortLabel.setText("Port: " + server.getPort());
			
			startGameButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					server.update(Message.GAME_STATE, "started");
					GameApp.getInstance().openRunningScreen(game);									
				}
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void updateObjectsEnabledStatus() {
		if (thisPlayer.isReady() && otherPlayer.isReady()) {
			startGameButton.setEnabled(true);
		} else {
			startGameButton.setEnabled(false);
		}
		
		if (thisPlayer.isReady()) {
			thisPlayerIsReadyLabel.setText("Ready");
			thisPlayerIsReadyLabel.setForeground(Color.green);
		} else {
			thisPlayerIsReadyLabel.setText("Not ready");
			thisPlayerIsReadyLabel.setForeground(Color.red);
		}
		
		if (otherPlayer.isReady()) {
			otherPlayerIsReadyLabel.setText("Ready");
			otherPlayerIsReadyLabel.setForeground(Color.green);
		} else {
			otherPlayerIsReadyLabel.setText("Not ready");
			otherPlayerIsReadyLabel.setForeground(Color.red);
		}
		
	}

	@Override
	public void update(Message messageType, Object value) {
		if (messageType == Message.IS_READY) {
			otherPlayer.setReady((boolean) value);
			updateObjectsEnabledStatus();
		} else if (messageType == Message.USERNAME) {
			otherPlayerLabel.setText(String.valueOf(value));
		}
	}
}
