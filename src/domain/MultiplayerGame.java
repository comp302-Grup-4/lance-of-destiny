package domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

import domain.animation.BarrierGrid;
import exceptions.InvalidBarrierNumberException;
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
		case USERNAME:
			otherPlayer.setUsername(String.valueOf(value));
			break;
		case BARRIER_GRID:
			byte[] byteArray = Base64.getDecoder().decode(String.valueOf(value));
			ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
			try {
				ObjectInputStream ois = new ObjectInputStream(bais);
				BarrierGrid grid = (BarrierGrid) ois.readObject();
				this.getAnimator().setBarrierGrid(grid);
			} catch (IOException | ClassNotFoundException | InvalidBarrierNumberException e) {
				e.printStackTrace();
			}
			break;
		case SPELL:
						
			break;
		
		default:
			break;
		}		
	}
	
}
