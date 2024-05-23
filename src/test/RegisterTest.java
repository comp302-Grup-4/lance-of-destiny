package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ui.RegisterScreen;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;

public class RegisterTest {

    private RegisterScreen registerScreen;
    private JTextField usernameField;
    private JPasswordField passwordField;

    @Before
    public void setUp() {
        registerScreen = new RegisterScreen();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        
        try {
            Files.deleteIfExists(Paths.get("users.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        } }
    //Requires:1-Username should be unique and not null
    //2- password should be valid in terms of length and content
    //Effects:Checks the username and password inputs and if they are valid shows a success 
    //dialog and register occur, if not gives error dialog and player may try again
    //Glass box test: 1-checking the username input ("Beyza", "123") existence
    //2- checking by testInvalidPassword() the input password ("ku1234") validness
    //3- checking by testSpecialChar() whether the password contain special char or not
    //Black box test: 1-if the input username and password are valid by testRegistration(), asserting 
    //a dialog box which says Successfully registered.
    @Test
    public void testEmptyUsernameAndPassword() {
        registerScreen.register(usernameField, passwordField);
        assertTrue(isMessageDialogDisplayed("Please fill in all fields."));
    }

    @Test
    public void testExistingUsername() {
        addTestUser("Beyza", "123");

        usernameField.setText("existing_username");
        passwordField.setText("password123");
        registerScreen.register(usernameField, passwordField);
        assertTrue(isMessageDialogDisplayed("Username already exists."));
    }

    @Test
    public void testInvalidPasswordFormat() {
        usernameField.setText("Merve");
        passwordField.setText("ku1234"); 
        registerScreen.register(usernameField, passwordField);
        assertTrue(isMessageDialogDisplayed("Password must be at least 8 characters long and not contain \",.:\""));
    }

    @Test
    public void testValidRegistration() {
        usernameField.setText("Merve");
        passwordField.setText("ku1234567");
        registerScreen.register(usernameField, passwordField);
        assertTrue(isMessageDialogDisplayed("Successfully registered."));
        assertTrue(isUserRegistered("Merve", "ku1234567"));
    }

    @Test
    public void testPasswordContainsSpecialCharacters() {
        usernameField.setText("Merve");
        passwordField.setText("ku.12345,"); 
        registerScreen.register(usernameField, passwordField);
        assertTrue(isMessageDialogDisplayed("Password must be at least 8 characters long and not contain \",.:\""));
    }
    private void addTestUser(String username, String password) {
        try {
            Files.write(Paths.get("users.txt"), (username + ":" + password + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean isMessageDialogDisplayed(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = pane.createDialog("Message");
        dialog.setVisible(true);
        String displayedMessage = (String) pane.getMessage();
        dialog.dispose();
        return message.equals(displayedMessage);
    }
    private boolean isUserRegistered(String username, String password) {
        try {
            return Files.lines(Paths.get("users.txt"))
                        .anyMatch(line -> line.equals(username + ":" + password));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }}}


