package prog.exception;

public class ObjectAlreadyExistsException extends GameRuntimeException{
	public ObjectAlreadyExistsException() { super(); }
	
	public ObjectAlreadyExistsException(String s) { super(s); }
}
