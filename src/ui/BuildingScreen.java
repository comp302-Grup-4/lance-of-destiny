package ui;

import java.awt.Color;
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
import javax.swing.border.LineBorder;

import domain.Game;
import domain.animation.BarrierGrid;
import exceptions.InvalidBarrierNumberException;
import ui.playview.BuildView;

public class BuildingScreen extends JPanel {

	private static final long serialVersionUID = 4L;
	private Game game;
	/**
	 * Create the panel.
	 */
	JPanel gridPanel, barrierPanel, buttonPanel;
	JLabel icon1, icon2, icon3, icon4, label1, label2, label3, label4;
	JTextField field1, field2, field3, field4;
	
	BarrierGrid bg;
	
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
		gridPanel.setBorder(new LineBorder(Color.black, 3));
		
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
		label1 = new JLabel("<html>Simple barriers</html>");
		label2 = new JLabel("<html>Firm barriers</html>");
		label3 = new JLabel("<html>Explosive barriers</html>");
		label4 = new JLabel("<html>Gift barriers</html>");
		field1 = new JTextField();
		field2 = new JTextField();
		field3 = new JTextField();
		field4 = new JTextField();
		
		field1.setText("75");
		field2.setText("10");
		field3.setText("5");
		field4.setText("10");

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

		cons.ipady = 0;
		
		barrierPanel.add(inputPanel, cons);
		
		
		//deneme
		JButton buildButton = new JButton("Build");
        
		
        // Add ActionListener to handle build button action
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call method to handle building barriers based on user input
            		try {
            		bg = new BarrierGrid(Integer.parseInt(field1.getText()), Integer.parseInt(field2.getText()), Integer.parseInt(field3.getText()), Integer.parseInt(field4.getText()));
            		game.getAnimator().setBarrierGrid(bg);
            		BuildView buildView = new BuildView(gridPanel, game);
            		gridPanel.setLayout(new GridBagLayout());
            		GridBagConstraints gbc = new GridBagConstraints();
            		
            		gbc.fill = GridBagConstraints.BOTH;
            		gbc.gridx = 0;
            		gbc.gridy = 0;
            		gbc.weightx = 1;
            		gbc.weighty = 1;

            		buildView.setFocusable(true);
            		buildView.setVisible(true);
            		
            		gridPanel.add(buildView, gbc);
            		gridPanel.revalidate();
                    gridPanel.repaint();
            	} catch (NumberFormatException | InvalidBarrierNumberException ex) {
                    // Handle exception and show error message
                    JOptionPane.showMessageDialog(BuildingScreen.this, "Invalid number of barriers", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Update GridBagConstraints to place buildButton in barrierPanel
        cons.weightx = 1;
        cons.weighty = 0;
        cons.gridx = 0;
        cons.gridy = 1;  // Placing the buildButton right after inputPanel

        // Add the buildButton to barrierPanel
        barrierPanel.add(buildButton, cons);
        //deneme end

		cons.weightx = 1;
		cons.weighty = 8;
		cons.gridx = 1;
		cons.gridy = 0;
		
		this.add(barrierPanel, cons);
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		cons.ipady = 0;
		cons.weightx = 0;
		cons.weighty = 1;
		cons.gridwidth = 2;
		cons.gridx = 0;
		cons.gridy = 1;
		buttonPanel.setBackground(new Color(30,55,120));
		
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
				try {
					game.getAnimator().setBarrierGrid(bg);	
					g.openRunningScreen(game);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidBarrierNumberException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
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
		try {
			game.loadBarrierGrid();
		} catch (Exception e) {
			JOptionPane.showInputDialog(this, "Barrier grid could not be loaded.");
		}
	}
	
	
	

}


