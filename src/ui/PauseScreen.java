package ui;
	import javax.swing.*;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;

	
	
	
	public class PauseScreen extends JPanel {
	    private boolean GameRunning;
	    private JButton pauseButton;
	    
	    
	    
	    
	    public PauseScreen() {
	        this.GameRunning = true;
	        Position();
	    }
	    private void Position() {
	        setLayout(null); 
	        pauseButton = new JButton("Pause (Press 'p')");
	        pauseButton.setBounds(10, 10, 120, 30);
	        add(pauseButton);
	        pauseButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                pauseGame();
	            } });}
	    public void pauseGame() {
	        this.GameRunning = false;
	        JOptionPane.showMessageDialog(this, "Game paused.\nMake a choice!:");
	        String[] buttons = {"Resume", "Save", "Help", "Quit"};
	        int option = JOptionPane.showOptionDialog(this, "Make a choice:", "Pause Menu",
	                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, buttons, buttons[0]);
	        switch (option) {
	        
	            case 0:
	                resumeGame();
	                break;
	                
	            case 1:
	                saveGame();
	                break;
	                
	            case 2:
	                showHelp();
	                break;
	                
	            case 3:
	                quitGame();
	                break;
	                
	            default:
	                System.out.println("You made an invalid choice.");
	        } }
	    public boolean GameRunning() {
	        return GameRunning;
	    }
	    private void resumeGame() {
	        this.GameRunning = true;
	        JOptionPane.showMessageDialog(this, "Game resumed.");
	        //buraya resume eklendiği zaman eklenecek
	    }
	    private void saveGame() {
	        JOptionPane.showMessageDialog(this, "Game saved.");
	        //buraya savegame eklendiği zaman eklenecek
	    }
	    private void showHelp() {
	        JOptionPane.showMessageDialog(this, "Help screen displayed.");
	        //help screen use caselerimizde var ama henüz class yok ileride silinebilir yapmayacaksak
	    }
	    private void quitGame() {
	        JOptionPane.showMessageDialog(this, "Quitting game.");
	        System.exit(0);
	        //sadeece quit burada yapılıyor
	    }
	    
	}

	


