package domain;

import java.io.Serializable;

public class Player implements Serializable{
	private static final long serialVersionUID = 322548287069839758L;
	protected int chances;
	protected int score;
	
	protected PlayerAccount account;
	
	public Player() {
		chances = 3;
		score = 0;
		this.account = new PlayerAccount("Unknown", "", null);
	}
	
	public Player(PlayerAccount account) {
		this();
		this.account = account;
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
	
	public PlayerAccount getAccount() {
		return account;
	}
	
	public String getUserName() {
		return this.account.getUserName();
	}
	
	public void setUsername(String username) {
		this.account.setUserName(username);
	}
	
	public boolean isReady() {
		return account.isReady;
	}
	
	public void setReady(boolean isReady) {
		account.isReady = isReady;
	}
}
