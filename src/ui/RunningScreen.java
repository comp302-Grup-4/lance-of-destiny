package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

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
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		this.setBounds(0, 0, width, height);
		
		this.setLayout(new GridBagLayout());
		
		playView = new PlayView(this, game);
		
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
