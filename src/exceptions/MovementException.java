package exceptions;

public class MovementException extends GameActionException {

	public MovementException() {
		super("Invalid Move!");
	}

	public MovementException(String message) {
		super(message);
	}

}
