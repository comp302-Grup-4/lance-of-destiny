package domain;

import network.Message;
import network.MultiplayerObserver;
import ui.GameApp;

public class MultiplayerGame extends Game implements MultiplayerObserver {
	private static final long serialVersionUID = -821375180377830072L;
	
	Player otherPlayer;
	
	public MultiplayerGame() {
		super();
	}
	
	public void setOtherPlayer(Player otherPlayer) {
		this.otherPlayer = otherPlayer;
	}
	
	public Player getOtherPlayer() {
		return otherPlayer;
	}
	
	@Override
	public void setPlayerScore(int newScore) {
		super.setPlayerScore(newScore);
		GameApp.getInstance().getActiveServer().update(Message.SCORE, newScore);
	}
	
	@Override
	public void update(Message messageType, Object value) {
		switch (messageType) {
		case SCORE:
			otherPlayer.setScore((int) value);
			break;
		case SPELL:
			break;
		
		default:
			break;
		}		
	}
	
}
