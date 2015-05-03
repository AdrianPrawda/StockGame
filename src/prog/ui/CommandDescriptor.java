package prog.ui;

import prog.interfaces.CommandTypeInfo;

public class CommandDescriptor {
	//Defined by a CommandTypeInfo and a list of parameters
	CommandTypeInfo cmdType;
	Object[] params;
	
	public void setCommandType(CommandTypeInfo commandType){
		cmdType = commandType;
	}
	
	public void setParams(Object[] params){
		this.params = params;
	}
	
	public CommandTypeInfo getCommandType(){
		return cmdType;
	}
	
	public Object[] getParams(){
		return params;
	}
}
