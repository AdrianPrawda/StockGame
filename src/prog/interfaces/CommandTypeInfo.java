package prog.interfaces;

import java.lang.reflect.Method;

public interface CommandTypeInfo {
	public String getName();
	
	public String getHelpText();
	
	public Class<?>[] getParamTypes();
	
	public Method getMethod();
	
	public Object getTarget();
}
