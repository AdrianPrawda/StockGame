package prog.ui;

import prog.interfaces.Executable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.IllegalAccessException;

import prog.interfaces.CommandTypeInfo;

public class Command extends CommandDescriptor implements Executable {

	@Override
	public Object execute() throws IllegalAccessException, InvocationTargetException{
		CommandTypeInfo c = super.cmdType;
		Method m = c.getMethod();
		Object o = null;
		
		try {
//			//Test
//			for(Object e : super.params){
//				System.out.println("e: " + e.getClass());
//			}
//			System.out.flush();
			
			o = m.invoke(c.getTarget(), super.params);
		} catch (IllegalAccessException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw e;
		}
		
		return o;
	}

}
