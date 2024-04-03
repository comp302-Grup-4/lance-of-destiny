package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.*;

public class RegisterScreen extends JPanel {

	private static final long serialVersionUID = 3L;

	public RegisterScreen() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		this.setBounds(0, 0, width / 2, height / 2);
		this.setLayout(null);

		JTextField usernameField = new JTextField();
		usernameField.setBounds(50, 50, 200, 30);
		this.add(usernameField);

		JTextField passwordField = new JTextField();
		passwordField.setBounds(50, 100, 200, 30);
		this.add(passwordField);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(50, 150, 100, 30);
		this.add(cancelButton);

		JButton registerButton = new JButton("Register");
		registerButton.setBounds(160, 150, 100, 30);
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addToTextFile(usernameField.getText(), passwordField.getText());
			}
		});

		this.add(registerButton);
	}

	private int addToTextFile(String username, String password) {
		try {
			if (!Files.exists(Paths.get("users.txt"))) {
				Files.createFile(Paths.get("users.txt"));
			}
			Files.write(Paths.get("users.txt"), (username + ":" + password + "\n").getBytes(), StandardOpenOption.APPEND);
			return 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	};

}
