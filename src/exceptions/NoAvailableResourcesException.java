package exceptions;

public class NoAvailableResourcesException extends GameActionException {

	public NoAvailableResourcesException() {
		super("Not Enough Resources!");
	}

	public NoAvailableResourcesException(String message) {
		super(message);
	}

}
