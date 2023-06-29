package exceptions;

public class NotEnoughActionsException extends GameActionException {

	public NotEnoughActionsException() {
		super("Not Enough Actions!");
	}

	public NotEnoughActionsException(String message) {
		super(message);
	}

}
