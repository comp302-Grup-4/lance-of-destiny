package domain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import domain.animation.*;
import domain.animation.spells.Spell;
import exceptions.InvalidBarrierNumberException;
import network.Message;
import network.MultiplayerObserver;
import ui.GameApp;

public class Game implements Serializable  {
	private static final long serialVersionUID = 7679992000960473271L;
	private static final long gameVersion = 2; // version 2 for phase II
	private Player player;
	private Animator animator;
	
	public Game() {
		player = new Player(GameApp.getInstance().getActivePlayerAccount());
		animator = new Animator(this);
	}
	
	public void saveGame(String fileName) throws IOException {
		FileOutputStream fileOutputStream
	      = new FileOutputStream(fileName + ".txt");
	    ObjectOutputStream objectOutputStream
	      = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(this);
	    objectOutputStream.flush();
	    objectOutputStream.close();
	}
	
	public Game loadGame(String name) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream
	      = new FileInputStream(name + ".txt");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    Game g = (Game) objectInputStream.readObject();
	    objectInputStream.close();
	    return g;

	}

	public void saveStats(String name) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(Path.of(name + ".st"));
		writer.write(player.getChances() + "\n");
		writer.write(player.getScore() + "\n");
		long timeElapsed = System.currentTimeMillis() - animator.getStartTimeMilli();
		writer.write(timeElapsed + "\n");
		writer.write(gameVersion + "\n"); // version number
		// Save Ymir's last two spells
		Ymir ymir = animator.getYmir();
		writer.write(ymir.getLastTwoSpells()[0] + "\n");
		writer.write(ymir.getLastTwoSpells()[1] + "\n");
		writer.close();
	}

	public int loadStats(String name) throws IOException {
		BufferedReader reader = Files.newBufferedReader(Path.of(name + ".st"));
		int chances = Integer.parseInt(reader.readLine());
		int score = Integer.parseInt(reader.readLine());
		int timeElapsed = Integer.parseInt(reader.readLine());
		long versionNumber = Long.parseLong(reader.readLine());
		if (versionNumber > gameVersion) {
			System.out.println("Save version is higher than current version.");
			return 1;
		}
		// Load Ymir's last two spells
		Ymir ymir = animator.getYmir();
		int[] lastTwoSpells = new int[2];
		String line1 = reader.readLine();
		String line2 = reader.readLine();
		if (line1 == null || line2 == null) {
			// Generate random spells if there are no more lines
			SecureRandom rand = new SecureRandom();
			int spellType[] = {Spell.INFINITE_VOID, Spell.DOUBLE_ACCEL, Spell.HOLLOW_PURPLE};
			lastTwoSpells[0] = spellType[rand.nextInt(spellType.length)];
			lastTwoSpells[1] = spellType[rand.nextInt(spellType.length)];
		} else {
			lastTwoSpells[0] = Integer.parseInt(line1);
			lastTwoSpells[1] = Integer.parseInt(line2);
		}
		ymir.setLastTwoSpells(lastTwoSpells);
		reader.close();
		player.setChances(chances);
		player.setScore(score);
		animator.setStartTimeMilli(System.currentTimeMillis() - timeElapsed);
		return 0;
	}

	public void saveBarrierGrid(String name) throws IOException {
		FileOutputStream fileOutputStream
				= new FileOutputStream(name + ".bg");
		ObjectOutputStream objectOutputStream
				= new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(animator.getBarrierGrid());
		objectOutputStream.flush();
		objectOutputStream.close();
	}


	public BarrierGrid loadBarrierGrid(String name) throws IOException, ClassNotFoundException, InvalidBarrierNumberException {
		FileInputStream fileInputStream
	      = new FileInputStream(name + ".bg");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    BarrierGrid bg = (BarrierGrid) objectInputStream.readObject();
	    objectInputStream.close();
	    animator.setBarrierGrid(bg);
	    return bg;
	}

	public void saveFireball(String name) throws IOException {
		FileOutputStream fileOutputStream
				= new FileOutputStream(name + ".fb");
		ObjectOutputStream objectOutputStream
				= new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(animator.getFireball());
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	public void loadFireball(String name) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream
	      = new FileInputStream(name + ".fb");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    animator.setFireball((FireBall) objectInputStream.readObject());
	    objectInputStream.close();
	}

	public void saveStaff(String name) throws IOException {
		FileOutputStream fileOutputStream
				= new FileOutputStream(name + ".stf");
		ObjectOutputStream objectOutputStream
				= new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(animator.getStaff());
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	public void loadStaff(String name) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream
	      = new FileInputStream(name + ".stf");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    animator.setStaff((MagicalStaff) objectInputStream.readObject());
	    objectInputStream.close();
	}

	// TODO save and load spells

	public void saveGameState(String name) throws IOException {
		if (name != null) {
			try {
				Path path = Paths.get("saved_games/" + name);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
				}

				String savePath = "saved_games/" + name + "/";
				saveStats(savePath);
				saveStaff(savePath);
				saveFireball(savePath);
				saveBarrierGrid(savePath);

			} catch (IOException e) {
				System.out.println("An error occurred while saving the game state.");
				e.printStackTrace();
			}
		}
	}

	public void loadGameState(String name) throws IOException {
		if (name != null) {
			try {
				String loadPath = "saved_games/" + name + "/";
				// cancel loading if version is higher than current version
				if (loadStats(loadPath) == 1) {
					return;
				}
				loadStaff(loadPath);
				loadFireball(loadPath);
				loadBarrierGrid(loadPath); // this should be last

			} catch (IOException e) {
				System.out.println("An error occurred while loading the game state.");
				e.printStackTrace();
			} catch (InvalidBarrierNumberException e) {
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void writeHighScore(int score) {
		// check if the file highscores exists, create if not
		Path path = Paths.get("highscores");
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				System.out.println("An error occurred while creating the highscores file.");
				e.printStackTrace();
			}
		}
		//append score to new line
		try {
			BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND);
			String username;
			if (player.getPlayerAccount() != null) {
				username = player.getPlayerAccount().getUserName();
			} else {
				username = "NULL";
			}
			writer.write(Integer.toString(score) + ":" + username);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred while writing the highscore.");
			e.printStackTrace();
		}
	}

	public Animator getAnimator() {
		return animator;
	}
	
	public int getPlayerChances() {
		return player.getChances();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getPlayerScore() {
		return player.getScore();
	}
	
	public void setPlayerScore(int newScore) {
		player.setScore(newScore);
	}
	
	public void endGame(String message, ImageIcon icon) {
		String[] option = {"Return to Main Menu"};
		JOptionPane.showOptionDialog(null, 
				message, 
				"Game Over!", 
				JOptionPane.OK_OPTION,
				JOptionPane.PLAIN_MESSAGE, 
				icon, 
				option,
				0);
		GameApp.getInstance().openMainMenuScreen();
	}
}
