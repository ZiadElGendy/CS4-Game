package exceptions;

public abstract class GameActionException extends Exception {

	public GameActionException() {
		super("Invalid Game Action!");
	}

	public GameActionException(String message) {
		super(message);
	}

}
