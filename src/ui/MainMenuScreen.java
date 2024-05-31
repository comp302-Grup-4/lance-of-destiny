package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.PriorityQueue;
import javax.swing.*;

import domain.Game;

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
				Game single_player = new Game();
				GameApp.getInstance().setActiveGame(single_player);
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

		viewHighScoresButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				Path path = Paths.get("highscores");
				PriorityQueue<String> scores = new PriorityQueue<>(Comparator.comparingInt(s -> -Integer.parseInt(s.split(":")[0])));
				try (BufferedReader reader = Files.newBufferedReader(path)) {
					String line;
					while ((line = reader.readLine()) != null) {
						scores.add(line);
					}
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				// display the first 5 high scores
				StringBuilder message = new StringBuilder();
				for (int i = 0; i < 5; i++) {
					if (scores.isEmpty()) {
						break;
					}
					message.append(scores.poll()).append("\n");
				}
				JOptionPane.showMessageDialog(null, message.toString(), "High Scores", JOptionPane.PLAIN_MESSAGE);
			}
		});

		menuPanel.add(singlePlayerButton);
		menuPanel.add(multiplayerButton);
		menuPanel.add(viewHighScoresButton);
		menuPanel.add(exitButton);
	}

}
