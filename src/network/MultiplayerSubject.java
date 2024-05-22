package network;

public interface MultiplayerSubject {
	void registerObserver(MultiplayerObserver observer);
	
	void removeObserver(MultiplayerObserver observer);
	
	void notifyObserver(Message messageType, Object value);
}
