package domain;

public class Player {
	private int chances;
	private int score;
	
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
}
