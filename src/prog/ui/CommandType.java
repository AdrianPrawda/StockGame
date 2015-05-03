package prog.ui;

import java.lang.reflect.Method;

import prog.interfaces.CommandTypeInfo;

public class CommandType implements CommandTypeInfo {
	String name;
	String helpText;
	Class<?>[] paramTypes;
	Method method;
	Object target;
	
	public CommandType(String name, String helpText, Method method, Object target, Class<?>... paramType){
		this.name = name;
		this.helpText = helpText;
		this.method = method;
		this.target = target;
		paramTypes = paramType;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHelpText() {
		return helpText;
	}

	@Override
	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object getTarget() {
		return target;
	}

}
