package domain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import domain.animation.Animator;
import domain.animation.BarrierGrid;
import domain.animation.SpellDepot;
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

	public void saveBarrierGrid(String name) throws IOException {
		FileOutputStream fileOutputStream
				= new FileOutputStream(name + ".txt");
		ObjectOutputStream objectOutputStream
				= new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(animator.getBarrierGrid());
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	public BarrierGrid loadBarrierGrid(String name) throws IOException, ClassNotFoundException, InvalidBarrierNumberException {
		FileInputStream fileInputStream
	      = new FileInputStream(name + ".txt");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    BarrierGrid bg = (BarrierGrid) objectInputStream.readObject();
	    objectInputStream.close();
	    animator.setBarrierGrid(bg);
	    return bg;
	}

	public void saveGameState(String name) throws IOException {
		if (name != null) {
			try {
				System.out.println("Saving barrier grid...");
				saveBarrierGrid(name);
				System.out.println("Barrier grid saved successfully.");

				System.out.println("Saving player chances and score...");
				BufferedWriter writer = Files.newBufferedWriter(Path.of(name + ".st"));
				int chances = player.getChances();
				int score = player.getScore();
				writer.write(chances + "\n");
				writer.write(score + "\n");
				writer.close();
				System.out.println("Player chances (" + chances + ") and score (" + score + ") saved successfully.");
			} catch (IOException e) {
				System.out.println("An error occurred while saving the game state.");
				e.printStackTrace();
			}
		}
	}

	public void loadGameState(String name) throws IOException {
		if (name != null) {
			try {
				System.out.println("Loading barrier grid...");
				loadBarrierGrid(name);
				System.out.println("Barrier grid loaded successfully.");

				System.out.println("Loading player chances and score...");
				BufferedReader reader = Files.newBufferedReader(Path.of(name + ".st"));
				int chances = Integer.parseInt(reader.readLine());
				int score = Integer.parseInt(reader.readLine());
				player.setChances(chances);
				player.setScore(score);
				System.out.println("Player chances (" + chances + ") and score (" + score + ") loaded successfully.");
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
