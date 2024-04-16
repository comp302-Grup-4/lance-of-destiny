package ui;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.Game;
import domain.animation.BarrierGrid;

public class GameApp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel runningScreen;
	private JPanel registerScreen;
	private JPanel loginScreen;
	private JPanel mainMenuScreen;
	private JPanel buildingScreen;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameApp frame = new GameApp();
					
					GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
					GraphicsDevice device = graphics.getDefaultScreenDevice();

					device.setFullScreenWindow(frame);
					
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GameApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 300);
		contentPane = new MainMenuScreen(this);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
	}
	
	public void openRunningScreen() {
		runningScreen = new RunningScreen(new Game());
		
		setContentPane(runningScreen);
		this.revalidate();
		this.repaint();
	}
	
	public void openRegisterScreen() {
		registerScreen = new RegisterScreen(this);
		setContentPane(registerScreen);
		this.revalidate();
		this.repaint();
	}

	public void openLoginScreen() {
		loginScreen = new LoginScreen(this);
		setContentPane(loginScreen);
		this.revalidate();
		this.repaint();
	}
	
	public void openMainMenuScreen() {
		mainMenuScreen = new MainMenuScreen(this);
		setContentPane(mainMenuScreen);
		
		this.revalidate();
		this.repaint();
	}
	
	public void openBuildingScreen() {
		buildingScreen = new BuildingScreen(this);
		setContentPane(buildingScreen);
		
		this.revalidate();
		this.repaint();
	}
	

	public void exitGame() {
		int result = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(1);
		}
	}
}
