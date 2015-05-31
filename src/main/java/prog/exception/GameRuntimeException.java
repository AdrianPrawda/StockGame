package prog.exception;

public class GameRuntimeException extends RuntimeException{
	public GameRuntimeException() { super(); }
	
	public GameRuntimeException(String s) { super(s); }

}
