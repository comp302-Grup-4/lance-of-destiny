package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.Game;
import domain.PlayerAccount;
import network.Message;
import network.MultiplayerObserver;
import network.Server;

public class GameApp extends JFrame implements MultiplayerObserver {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel runningScreen;
	private JPanel registerScreen;
	private JPanel loginScreen;
	private JPanel mainMenuScreen;
	private JPanel buildingScreen;
	private JPanel connectionScreen;
	private JPanel hostWaitingScreen;
	private JPanel clientWaitingScreen;
	
	private Game activeGame;
	private PlayerAccount activePlayerAccount = new PlayerAccount("Jane", "Doe", null);
	private static GameApp instance = null;
	
	private Server activeServer;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					instance = new GameApp();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					final int H = screenSize.height;
					final int W = screenSize.width;
					instance.setSize(W, H);
//					GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
//					GraphicsDevice device = graphics.getDefaultScreenDevice();
//
//					device.setFullScreenWindow(frame);

					instance.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private GameApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 450, 300);
		contentPane = new MainMenuScreen();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
	}
	
	public static GameApp getInstance() {
		return instance;
	}
	
	public void openRunningScreen(Game g) {
		runningScreen = new RunningScreen(g);
		
		setContentPane(runningScreen);
		this.revalidate();
		this.repaint();
	}
	
	public void openRegisterScreen() {
		registerScreen = new RegisterScreen();
		setContentPane(registerScreen);
		this.revalidate();
		this.repaint();
	}

	public void openLoginScreen() {
		loginScreen = new LoginScreen();
		setContentPane(loginScreen);
		this.revalidate();
		this.repaint();
	}
	
	public void openMainMenuScreen() {
		mainMenuScreen = new MainMenuScreen();
		setContentPane(mainMenuScreen);
		
		this.revalidate();
		this.repaint();
	}
	
	public void openBuildingScreen() {
		buildingScreen = new BuildingScreen();
		setContentPane(buildingScreen);
		
		this.revalidate();
		this.repaint();
	}
	
	public void openConnectionScreen() {
		connectionScreen = new ConnectionScreen();
		setContentPane(connectionScreen);
		
		this.revalidate();
		this.repaint();
	}
	
	public void openHostWaitingScreen() {
		hostWaitingScreen = new HostWaitingScreen();
		setContentPane(hostWaitingScreen);
		
		this.revalidate();
		this.repaint();
	}
	
	public void openClientWaitingScreen() {
		clientWaitingScreen = new ClientWaitingScreen();
		setContentPane(clientWaitingScreen);
		
		this.revalidate();
		this.repaint();
	}
	
	public PlayerAccount getActivePlayerAccount() {
		return activePlayerAccount;
	}
	
	public void setActivePlayerAccount(String userName, String password) {
		this.activePlayerAccount = new PlayerAccount(userName, password, null);
	}
	
	public Game getActiveGame() {
		return activeGame;
	}
	
	public void setActiveGame(Game activeGame) {
		this.activeGame = activeGame;
	}
	
	public void setActiveServer(Server server) {
		this.activeServer = server;
	}
	
	public Server getActiveServer() {
		return activeServer;
	}

	public void exitGame() {
		int result = JOptionPane.showConfirmDialog(this, "Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(1);
		}
	}

	@Override
	public void update(Message messageType, Object value) {
		switch (messageType) {
		case CONNECTION_ERROR:
			setActiveGame(null);
			setActiveServer(null);
			openMainMenuScreen();
			break;
		case GAME_OVER:
			openMainMenuScreen();
		default:
			break;
		}
	}
}
