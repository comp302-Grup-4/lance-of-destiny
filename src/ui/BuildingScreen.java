package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import domain.Game;
import domain.animation.SimpleBarrier;

public class BuildingScreen extends JPanel {

	private static final long serialVersionUID = 4L;
	private Game game;
	/**
	 * Create the panel.
	 */
	JPanel gridPanel, barrierPanel, buttonPanel;
	JLabel icon1, icon2, icon3, icon4, label1, label2, label3, label4;
	JTextField field1, field2, field3, field4;
	
	public BuildingScreen(GameApp g) {
		game = new Game();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		this.setBounds(0, 0, width, height);
		this.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		
		gridPanel = new JPanel();
		gridPanel.setBackground(Color.gray);
		
		cons.weightx = 50;
		cons.gridx = 0;
		cons.gridy = 0;
		
		this.add(gridPanel, cons);
		
		
		barrierPanel = new JPanel();
		barrierPanel.setLayout(new GridBagLayout());
		
		JPanel inputPanel = new JPanel(new GridLayout(4,3,10,10));
		icon1 = new JLabel(new ImageIcon("res/drawable/largeBlueGem.png"));
		icon2 = new JLabel(new ImageIcon("res/drawable/largeFirm.png"));
		icon3 = new JLabel(new ImageIcon("res/drawable/largeRedGem.png"));
		icon4 = new JLabel(new ImageIcon("res/drawable/largeGreenGem.png"));
		label1 = new JLabel("Number of simple obstacles");
		label2 = new JLabel("Number of firm obstacles");
		label3 = new JLabel("Number of explosive obstacles");
		label4 = new JLabel("Number of gift obstacles");
		field1 = new JTextField();
		field2 = new JTextField();
		field3 = new JTextField();
		field4 = new JTextField();

		inputPanel.add(icon1);
		inputPanel.add(label1);
		inputPanel.add(field1);
		inputPanel.add(icon2);
		inputPanel.add(label2);
		inputPanel.add(field2);
		inputPanel.add(icon3);
		inputPanel.add(label3);
		inputPanel.add(field3);
		inputPanel.add(icon4);
		inputPanel.add(label4);
		inputPanel.add(field4);
		inputPanel.setBackground(Color.blue);

		cons.ipady = 0;
		
		barrierPanel.add(inputPanel, cons);

		cons.weightx = 1;
		cons.weighty = 8;
		cons.gridx = 1;
		cons.gridy = 0;
		barrierPanel.setBackground(Color.CYAN);
		
		this.add(barrierPanel, cons);
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cons.ipady = 0;
		cons.weightx = 0;
		cons.weighty = 1;
		cons.gridwidth = 2;
		cons.gridx = 0;
		cons.gridy = 1;
		buttonPanel.setBackground(Color.green);
		
		JButton saveButton = new JButton("Save");
		
		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				saveGrid();
			}
		});
		
		JButton loadButton = new JButton("Load");
		
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadSavedGrids();
			}
		});
		
		JButton playButton = new JButton("Play Game");
		
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.openRunningScreen();
			}
		});
		
		JButton exitButton = new JButton("Exit");
	
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.exitGame();
			}
		});
		

		buttonPanel.add(saveButton);
		buttonPanel.add(loadButton);
		buttonPanel.add(playButton);
		buttonPanel.add(exitButton);
		
		
		this.add(buttonPanel, cons);
	}
	
	private void saveGrid() {
		System.out.println("save");
		try {
			game.saveBarrierGrid();
		} catch (IOException e) {
			JOptionPane.showInputDialog(this, "Barrier grid could not be saved.");
		}
	}
	
	private void loadSavedGrids() {
		System.out.println("load");
	}

}
