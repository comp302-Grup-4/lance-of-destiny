package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

import domain.Game;
import ui.playview.PlayView;

public class RunningScreen extends JPanel {	
	private static final long serialVersionUID = 5L;
	private PlayView playView;
	/**
	 * Create the panel.
	 */
	public RunningScreen(Game game) {
		playView = new PlayView(this, game);
		game.getAnimator();
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;

		playView.setFocusable(true);
		playView.setVisible(true);
		
		this.add(playView, gbc);
	}
}
