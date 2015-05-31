package prog.exception;

public class InvalidNumberException extends GameRuntimeException{
	public InvalidNumberException() { super(); }
	
	public InvalidNumberException(String s) { super(s); }
}
