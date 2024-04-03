package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class LoginScreen extends JPanel  {

	private GameApp g;
	private static final long serialVersionUID = 2L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	final int H = screenSize.height / 2;
	final int W = screenSize.width / 4;

	/**
	 * Create the panel.
	 */
	public LoginScreen(GameApp g) {
		this.g = g;
		if (!Files.exists(Paths.get("users.txt"))) {
		try {
			Files.createFile(Paths.get("users.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		this.setBounds(0, 0, W, H);
		this.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		JTextField usernameField = new JTextField(20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(usernameField, constraints);

		JPasswordField passwordField = new JPasswordField(20);
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(passwordField, constraints);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        
		constraints.gridx = 0;
		constraints.gridy = 2;
		this.add(cancelButton, constraints);

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login(usernameField, passwordField);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 3;
		this.add(loginButton, constraints);

		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login(usernameField, passwordField);
				}
			}
		});
	}


		private void login(JTextField usernameField, JPasswordField passwordField) {
			
			if (usernameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
				JOptionPane.showMessageDialog(this, "Please fill in all fields.");
				
				return;
			}

			if (usernameExists(usernameField.getText())) {
				boolean validated = validateCredentials(usernameField.getText(), (new String (passwordField.getPassword())));
				if (validated) {
					System.out.println(":)");
					//g.setContentPane(new MainMenuScreen());
				} else {
					JOptionPane.showMessageDialog(this, "No user found.");
				}
			}
			else {
				 
				JOptionPane.showMessageDialog(this, "Username does not exist.");
			}
		}


		private boolean usernameExists(String username) {
			try {
				return Files.lines(Paths.get("users.txt"))
						.map(line -> line.split(":")[0])
						.anyMatch(existingUsername -> existingUsername.equals(username));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		private boolean validateCredentials(String username, String password) {
			try {
				return Files.lines(Paths.get("users.txt"))
						.map(line -> line.split(":"))
						.anyMatch(credentials -> credentials[0].equals(username) && credentials[1].equals(password));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}


}
