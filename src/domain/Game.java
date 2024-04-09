package domain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import domain.animation.Animator;
import domain.animation.BarrierGrid;
import domain.animation.SpellDepot;
import domain.animation.barriers.Barrier;

public class Game {
	private Player player;
	private SpellDepot spellDepot;	
	private Animator animator;
	
	private int gameMode; // 0 for building, 1 for running
	
	public Game() {
		gameMode = 0;
		player = new Player();
		spellDepot = new SpellDepot();
		animator = new Animator();
	}
	
	public void saveGame() throws IOException {
		FileOutputStream fileOutputStream
	      = new FileOutputStream("gameInstance.txt");
	    ObjectOutputStream objectOutputStream 
	      = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(this);
	    objectOutputStream.flush();
	    objectOutputStream.close();
	}
	
	public void saveBarrierGrid() throws IOException {
		FileOutputStream fileOutputStream
	      = new FileOutputStream("barrierGridInstance.txt");
	    ObjectOutputStream objectOutputStream 
	      = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(animator.getBarrierGrid());
	    objectOutputStream.flush();
	    objectOutputStream.close();
	}
	
	public Game loadGame() throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream
	      = new FileInputStream("gameInstance.txt");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    Game g = (Game) objectInputStream.readObject();
	    objectInputStream.close();
	    return g;
		
	}

	public BarrierGrid loadBarrierGrid() throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream
	      = new FileInputStream("gameInstance.txt");
	    ObjectInputStream objectInputStream
	      = new ObjectInputStream(fileInputStream);
	    BarrierGrid bg = (BarrierGrid) objectInputStream.readObject();
	    objectInputStream.close();
	    return bg;
	}
	
	public Animator getAnimator() {
		return animator;
	}
}
