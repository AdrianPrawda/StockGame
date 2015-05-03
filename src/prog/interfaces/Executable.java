package prog.interfaces;

import java.lang.reflect.InvocationTargetException;

public interface Executable {
	public Object execute() throws IllegalAccessException, InvocationTargetException;
}
