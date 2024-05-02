package domain;

import java.io.*;
import domain.animation.Animator;
import domain.animation.BarrierGrid;
import exceptions.InvalidBarrierNumberException;

public class Game implements Serializable {
	private static final long serialVersionUID = 7679992000960473271L;
	private Player player;
	private Animator animator;
	
	private int gameMode; // 0 for building, 1 for running
	
	public Game() {
		gameMode = 0;
		player = new Player();
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
	
	public void saveBarrierGrid(String name) throws IOException {
		FileOutputStream fileOutputStream
	      = new FileOutputStream(name + ".txt");
	    ObjectOutputStream objectOutputStream 
	      = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(animator.getBarrierGrid());
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
	
	public Animator getAnimator() {
		return animator;
	}
	
	public int getPlayerChances() {
		return player.getChances();
	}
	
	public Player getPlayer() {
		return player;
	}
//
//	public void saveGame() {
//		String filename = JOptionPane.showInputDialog(null, "Enter filename to save:");
//		if (filename != null) {
//			String str = this.animator.getBarrierGrid().barrierListToString();
//			try {
//				BufferedWriter writer = new BufferedWriter(new FileWriter(filename + ".sav"));
//				writer.write(str);
//				writer.close();
//			} catch (IOException i) {
//				i.printStackTrace();
//			}
//		}
//	}

//	public String loadGame() throws IOException {
//		String filename = JOptionPane.showInputDialog(null, "Enter filename to load:");
//		if (filename != null) {
//			String content = Files.readString(Path.of(filename + ".sav"));
//			return content;
//		}
//		return null;
//	}
}
