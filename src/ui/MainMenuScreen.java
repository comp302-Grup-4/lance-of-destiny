package ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuScreen extends JPanel {
	private JButton playButton;
	private JButton viewHighScoresButton;
	private JButton exitButton;
	
	public MainMenuScreen(GameApp g) {
		this.setLayout(new GridBagLayout());
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.ipadx = 10;
		gbc.ipady = 10;
		this.add(menuPanel, gbc);
		
		playButton = new JButton("Play");
		viewHighScoresButton = new JButton("View High Scores");
		exitButton = new JButton("Exit");
		
		playButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				g.openBuildingScreen();
			}
		});
		
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				g.exitGame();
			}
		});
		
		menuPanel.add(playButton);
		menuPanel.add(viewHighScoresButton);
		menuPanel.add(exitButton);
	}

}
