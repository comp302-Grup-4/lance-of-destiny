package exceptions;

public class InvalidBarrierPositionException extends Exception {	
	public InvalidBarrierPositionException(int x, int y, int size_x, int size_y) {
		super(String.format("Invalid barrier position (%d, %d) for barrier grid of size (%d, %d).",
				x, y, size_x, size_y));
	}
}
