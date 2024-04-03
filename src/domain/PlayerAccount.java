package domain;

import java.util.List;

public class PlayerAccount {
	private String userName;
	private String userPassword;
	private List <Integer> highScores;
	
	public PlayerAccount(String userName, String userPassword, List<Integer> highScores) {
		super();
		this.userName = userName;
		this.userPassword = userPassword;
		this.highScores = highScores;
	}
	
	public void addNewScore(Integer newScore) {
		highScores.add(newScore);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public void associateGame(Game game) {
		
	}
}
