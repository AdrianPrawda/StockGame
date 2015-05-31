package prog.interfaces;

import java.lang.reflect.InvocationTargetException;

//Makes sure that all implementing classes have a execute() method
public interface Executable {
	public Object execute() throws IllegalAccessException, InvocationTargetException;
}
