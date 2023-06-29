package exceptions;

public class InvalidTargetException extends GameActionException {

	public InvalidTargetException() {
		super("Invalid Target!");
	}

	public InvalidTargetException(String message) {
		super(message);
	}

}
