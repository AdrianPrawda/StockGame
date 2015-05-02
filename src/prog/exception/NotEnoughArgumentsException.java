package prog.exception;

public class NotEnoughArgumentsException extends GameRuntimeException {
	public NotEnoughArgumentsException() { super(); }
	
	public NotEnoughArgumentsException(String s) { super(s); }
}
