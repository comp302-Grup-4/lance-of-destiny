package domain;

public class Player {
	private int chances;
	private int score;
	
	public Player() {
		
	}
	
	public int getChances() {
		return this.chances;
	}
	
	public void incrementChances() {
		this.chances++;
	}
	public void decrementChances() {
		this.chances--;
	}
	
}
