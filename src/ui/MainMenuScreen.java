package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuScreen extends JPanel {
	private static final long serialVersionUID = -6398901547224782581L;
	
	private JButton singlePlayerButton;
	private JButton multiplayerButton;
	private JButton viewHighScoresButton;
	private JButton exitButton;
	
	public MainMenuScreen() {
		this.setLayout(new GridBagLayout());
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.ipadx = 10;
		gbc.ipady = 10;
		this.add(menuPanel, gbc);
		
		singlePlayerButton = new JButton("Single Player");
		viewHighScoresButton = new JButton("View High Scores");
		exitButton = new JButton("Exit");
		multiplayerButton = new JButton("Multiplayer");
				
		singlePlayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				GameApp.getInstance().openBuildingScreen();
			}
		});
		
		multiplayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				GameApp.getInstance().openConnectionScreen();
			}
		});
		
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				GameApp.getInstance().exitGame();
			}
		});
		
		menuPanel.add(singlePlayerButton);
		menuPanel.add(multiplayerButton);
		menuPanel.add(viewHighScoresButton);
		menuPanel.add(exitButton);
	}

}
