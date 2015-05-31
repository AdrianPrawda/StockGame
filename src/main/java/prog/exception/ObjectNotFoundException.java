package prog.exception;

public class ObjectNotFoundException extends GameRuntimeException{
	public ObjectNotFoundException() { super(); }
	
	public ObjectNotFoundException(String s) { super(s); }
}
