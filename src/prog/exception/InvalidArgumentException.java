package prog.exception;

public class InvalidArgumentException extends GameRuntimeException {
	public InvalidArgumentException() { super(); }
	
	public InvalidArgumentException(String s) { super(s); }
}
