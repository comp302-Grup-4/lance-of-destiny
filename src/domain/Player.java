package domain;

import java.io.Serializable;

public class Player implements Serializable{
	private static final long serialVersionUID = 322548287069839758L;
	private int chances;
	private int score;
	private PlayerAccount pa; // TODO idk do stuff
	
	public Player() {
		chances = 3;
		score = 0;
	}
	
	public int getChances() {
		return this.chances;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void incrementChances() {
		this.chances++;
	}
	
	public void decrementChances() {
		this.chances--;
	}

	public void setChances(int chances) {
		this.chances = chances;
	}

	public PlayerAccount getPlayerAccount() {
		return pa;
	}
}
