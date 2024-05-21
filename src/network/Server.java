package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;

import domain.Game;
import domain.Player;
import ui.GameApp;

public abstract class Server implements MultiplayerSubject, MultiplayerObserver {
	String serverIPAddress;
	int serverPort = 12345;
	
	Player thisPlayer;
	Player otherPlayer;
	
	protected MessageSender messageSender;
	protected BufferedReader bufferedReader;
	
	HashSet<MultiplayerObserver> observers = new HashSet<>();

	Game game;

	String serverMessage;
	
	public Server() throws IOException {
		thisPlayer = new Player(GameApp.getInstance().getActivePlayerAccount());
		otherPlayer = new Player();
	}
	
	protected String getServerMessage() throws IOException {
		serverMessage = bufferedReader.readLine();
		return serverMessage;
	}
	
	protected void listenAndProcessMessages() throws IOException {
		String messages;
		while (true) {
			if ((messages = getServerMessage()) != null) {
				for (String message : messages.split(";")) {
					Message messageType = Message.valueOf(message.split(" ")[0]);
					String valueString = message.split(" ")[1];
					Object value = null;
					
					switch (messageType) {
					case IS_READY:
						value = Boolean.valueOf(valueString);
						break;
					case SCORE:
						value = Integer.valueOf(valueString);
						break;
					case SPELL:
						value = Integer.valueOf(valueString);
						break;
					case USERNAME:
						value = valueString;
						otherPlayer.setUsername(valueString);
						break;
					case GAME_STATE:
						value = String.valueOf(valueString);
						break;
					default:
						break;
					}
					
					notifyObserver(messageType, value);
				}
			}
		}
	}
	
	@Override
	public void update(Message messageType, Object value) {
		if (messageSender != null) {
			messageSender.put(messageType, String.valueOf(value));
			try {
				messageSender.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void registerObserver(MultiplayerObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(MultiplayerObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObserver(Message messageType, Object value) {
		observers.stream().forEach(x -> x.update(messageType, value));
	}

	public String getServerIPAddress() {
		return serverIPAddress;
	}

	public int getPort() {
		return serverPort;
	}

}
