package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.*;

public class RegisterScreen extends JPanel {

	private static final long serialVersionUID = 3L;
	private final ui.GameApp g = GameApp.getInstance();

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	final int H = screenSize.height / 2;
	final int W = screenSize.width / 4;

	public RegisterScreen() {
		this.setBounds(0, 0, W, H);
		this.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;

		JTextField usernameField = new JTextField(20);
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(usernameField, constraints);

		JLabel usernameLabel = new JLabel("Username:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add(usernameLabel, constraints);

		JPasswordField passwordField = new JPasswordField(20);
		constraints.gridx = 1;
		constraints.gridy = 1;
		this.add(passwordField, constraints);

		JLabel passwordLabel = new JLabel("Password:");
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add(passwordLabel, constraints);

		JButton registerButton = new JButton("Register");
		constraints.gridwidth = 2;
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				register(usernameField, passwordField);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 2;
		this.add(registerButton, constraints);
		
		JButton cancelButton = new JButton("Return to Login");
		constraints.gridwidth = 2;
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.openLoginScreen();
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 3;
		this.add(cancelButton, constraints);

		JButton exitButton = new JButton("Exit");
		constraints.gridwidth = 2;
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				g.exitGame();
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 4;
		this.add(exitButton, constraints);

		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					register(usernameField, passwordField);
				}
			}
		});
	}

	private void register(JTextField usernameField, JPasswordField passwordField) {
		if (usernameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields.");
			return;
		}
		if (usernameExists(usernameField.getText())) {
			JOptionPane.showMessageDialog(this, "Username already exists.");
		} else {
			if (!checkPassword(new String(passwordField.getPassword()))) {
				JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and not contain \",.:\"");
				return;
			} else {
				addToTextFile(usernameField.getText(), new String(passwordField.getPassword()));
				JOptionPane.showMessageDialog(this, "Successfully registered.");
			}
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


	private boolean checkCredentials(String username, String password) {
		try {
			return Files.lines(Paths.get("users.txt"))
					.map(line -> line.split(":"))
					.anyMatch(credentials -> credentials[0].equals(username) && credentials[1].equals(password));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private int addToTextFile(String username, String password) {
		try {
			if (!Files.exists(Paths.get("users.txt"))) {
				Files.createFile(Paths.get("users.txt"));
			}
			Files.write(Paths.get("users.txt"), (username + ":" + password + "\n").getBytes(), StandardOpenOption.APPEND);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private boolean checkPassword(String password) {
		// password doesnt contain ",.:" or any spaces and is at least 8 characters long
		return password.length() >= 8 && !password.contains(",") && !password.contains(".") && !password.contains(":") && !password.contains(" ");
	}
}
