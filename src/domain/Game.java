package domain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import domain.animation.*;
import exceptions.InvalidBarrierNumberException;

import javax.swing.*;

public class Game implements Serializable {
	private static final long serialVersionUID = 7679992000960473271L;
	private Player player;
	private SpellDepot spellDepot;	
	private Animator animator;
	
	private int gameMode; // 0 for building, 1 for running
	
	public Game() {
		gameMode = 0;
		player = new Player();
		spellDepot = new SpellDepot();
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
		writer.close();
	}

	public void loadStats(String name) throws IOException {
		BufferedReader reader = Files.newBufferedReader(Path.of(name + ".st"));
		player.setChances(Integer.parseInt(reader.readLine()));
		player.setScore(Integer.parseInt(reader.readLine()));
		animator.setStartTimeMilli(System.currentTimeMillis() - Long.parseLong(reader.readLine()));
		reader.close();
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
				loadStats(loadPath);
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

	public Animator getAnimator() {
		return animator;
	}
	
	public int getPlayerChances() {
		return player.getChances();
	}
	
	public Player getPlayer() {
		return player;
	}
}
