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

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	final int H = screenSize.height / 2;
	final int W = screenSize.width / 4;

	public RegisterScreen() {
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

		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				register(usernameField, passwordField);
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 3;
		this.add(registerButton, constraints);

		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					register(usernameField, passwordField);
				}
			}
		});

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(W, H);
		frame.setVisible(true);
	}

	private void register(JTextField usernameField, JPasswordField passwordField) {
		if (usernameField.getText().isEmpty() || passwordField.getPassword().length == 0) {
			JOptionPane.showMessageDialog(null, "Please fill in all fields.");
			return;
		}
		if (usernameExists(usernameField.getText())) {
			JOptionPane.showMessageDialog(null, "Username already exists.");
		} else {
			addToTextFile(usernameField.getText(), new String(passwordField.getPassword()));
			JOptionPane.showMessageDialog(null, "Successfully registered.");
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
}