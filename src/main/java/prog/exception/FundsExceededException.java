package prog.exception;

public class FundsExceededException extends GameException{
	public FundsExceededException() { super(); }
	
	public FundsExceededException(String s) { super(s); }
}
