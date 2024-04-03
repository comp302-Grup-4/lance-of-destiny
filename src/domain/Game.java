package domain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import domain.animation.Animator;
import domain.animation.BarrierGrid;
import domain.animation.SpellDepot;

public class Game {
	private Player player;
	private SpellDepot spellDepot;
	private BarrierGrid barrierGrid;
	
	private Animator animator;
	
	private int gameMode; // 0 for building, 1 for running
	
	public Game() {
		gameMode = 0;
		player = new Player();
		spellDepot = new SpellDepot();
		barrierGrid = new BarrierGrid();
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
	    objectOutputStream.writeObject(barrierGrid);
	    objectOutputStream.flush();
	    objectOutputStream.close();
	}
}
