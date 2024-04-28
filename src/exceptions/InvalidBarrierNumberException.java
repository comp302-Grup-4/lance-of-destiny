package exceptions;

public class InvalidBarrierNumberException extends Exception {
	public InvalidBarrierNumberException(String barrierType) {
		super("Invalid barrier number: " + barrierType);
	}
}
