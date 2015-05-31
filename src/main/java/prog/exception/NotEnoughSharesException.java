package prog.exception;

public class NotEnoughSharesException extends GameException{
	public NotEnoughSharesException() { super(); }
	
	public NotEnoughSharesException(String s) { super(s); }
}
