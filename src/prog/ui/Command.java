package prog.ui;

import prog.interfaces.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.IllegalAccessException;

import prog.interfaces.CommandTypeInfo;

public class Command extends CommandDescriptor implements Executable {

	@Override
	public Object execute() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		//get command type from superclass
		CommandTypeInfo c = super.cmdType;
		Method m = c.getMethod();
		Object o = null;
		
		//Invoke method on a target using the parameter from the superclass
		o = m.invoke(c.getTarget(), super.params);
	
		return o;
	}

}
