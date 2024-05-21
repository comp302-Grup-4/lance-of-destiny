package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Game;
import domain.MultiplayerGame;
import domain.Player;
import domain.PlayerAccount;
import network.Message;
import network.MultiplayerObserver;

public class ClientWaitingScreen extends JPanel implements MultiplayerObserver {
	private static final long serialVersionUID = 5022700988610570459L;
	
	Player thisPlayer = null;
	Player otherPlayer = null;
	JLabel thisPlayerLabel, otherPlayerLabel;
	JButton getReadyButton;
	JButton startGameButton;
	JLabel thisPlayerIsReadyLabel, otherPlayerIsReadyLabel;
	
	Font serifBoldFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	Font monospacedFont = new Font(Font.MONOSPACED, Font.PLAIN, 20);
	Font serifNormalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
	
	JLabel hostIPLabel, hostPortLabel;
	MultiplayerGame game;
	
	public ClientWaitingScreen()  {
		this.game = (MultiplayerGame) GameApp.getInstance().getActiveGame();
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		thisPlayer = new Player(GameApp.getInstance().getActivePlayerAccount());
		otherPlayer = new Player();
		otherPlayer.getPlayerAccount().setUserName("Waiting for connection...");
		game.setOtherPlayer(otherPlayer);
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(3, 2));

		thisPlayerLabel = new JLabel(thisPlayer.getUserName());
		innerPanel.add(thisPlayerLabel);
		
		if (otherPlayer == null) {
			otherPlayerLabel = new JLabel("Waiting for connection...");
		} else {
			otherPlayerLabel = new JLabel(otherPlayer.getUserName());
		}
		innerPanel.add(otherPlayerLabel);

		thisPlayerIsReadyLabel = new JLabel("Not ready");
		innerPanel.add(thisPlayerIsReadyLabel);
		
		otherPlayerIsReadyLabel = new JLabel("Not ready");
		innerPanel.add(otherPlayerIsReadyLabel);
		
		getReadyButton = new JButton("Get Ready");
		innerPanel.add(getReadyButton);
		
		getReadyButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				thisPlayer.setReady(!thisPlayer.isReady());
				if (thisPlayer.isReady()) {
					getReadyButton.setText("Unready");
				} else {
					getReadyButton.setText("Get Ready");
				}
				GameApp.getInstance().getActiveServer().update(Message.IS_READY, thisPlayer.isReady());
				updateObjectsEnabledStatus();
			}
		});

		gbc.weighty = 0.3;
		gbc.gridy = 1;
		
		this.add(innerPanel, gbc);
		
		updateObjectsEnabledStatus();
		GameApp.getInstance().getActiveServer().registerObserver(this);
	}
	
	public void updateObjectsEnabledStatus() {
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
		} else if (messageType == Message.GAME_STATE) {
			String gameState = String.valueOf(value);
			if (gameState.equals("started")) {
				GameApp.getInstance().openRunningScreen(game);
			}
		}
	}
	
}
