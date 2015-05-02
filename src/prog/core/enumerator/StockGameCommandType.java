package prog.core.enumerator;

import prog.interfaces.CommandTypeInfo;

public enum StockGameCommandType implements CommandTypeInfo{
	HELP("help",""," * list all commands"),
	EXIT("exit",""," * exit program"),
	CREATEPLAYER("crp","createPlayer","<name> * create new player", String.class),
	BUYSHARE("bs","buyShares","<player> <sharename> <amount> * buy that amount of shares", String.class, String.class, int.class),
	SELLSHARE("ss","sellShare", "<player> <sharename> <amount> * sell that amount of shares", String.class, String.class, int.class),
	LISTPLAYERS("lsp","playerList"," * list all registered players");
//	LISTSHARES("lss", " * list all available shares");

	private String cmdName;
	private String methodName;
	private String helpText;
	private Class<?>[] classes;
	
	private StockGameCommandType(String cmdName, String methodName, String helpText, Class<?>... clazz){
		this.cmdName = cmdName;
		this.methodName = methodName;
		this.helpText = helpText;
		classes = clazz;
	}
	
	public String getName(){
		return cmdName;
	}
	
	public String getMethodName(){
		return methodName;
	}
	
	public String getHelpText(){
		return helpText;
	}
	
	public Class<?>[] getParamTypes(){
		return classes;
	}
}
