package prog.exception;

public class WrongShareException extends GameRuntimeException{
	public WrongShareException() { super(); }
	
	public WrongShareException(String s) { super(s); }
}
