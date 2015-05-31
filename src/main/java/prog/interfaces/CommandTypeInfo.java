package prog.interfaces;

import java.lang.reflect.Method;

public interface CommandTypeInfo {
	//Command name
	public String getName();
	
	//Command description / help text
	public String getHelpText();
	
	//All parameter types as class object
	public Class<?>[] getParamTypes();
	
	//Method used on target
	public Method getMethod();
	
	//Target of the command
	public Object getTarget();
}
