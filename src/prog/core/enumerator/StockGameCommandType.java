package prog.core.enumerator;

import prog.interfaces.CommandTypeInfo;

public enum StockGameCommandType implements CommandTypeInfo{
	HELP("help"," * list all commands"),
	EXIT("exit"," * exit program"),
	CREATEPLAYER("crp","<name> * create new player", String.class),
	BUYSHARE("bs","<player> <sharename> <amount> * buy that amount of shares", String.class, String.class, int.class),
	SELLSHARE("ss", "<player> <sharename> <amount> * sell that amount of shares", String.class, String.class, int.class),
	LISTPLAYERS("lsp"," * list all registered players"),
	LISTSHARES("lss", " * list all available shares");

	private String cmdName;
	private String helpText;
	private Class<?>[] classes;
	
	private StockGameCommandType(String cmdName, String helpText){
		this.cmdName = cmdName;
		this.helpText = helpText;
		
		Class<?>[] c = {null};
		classes = c;
	}
	
	private StockGameCommandType(String cmdName, String helpText, Class<?> clazz){
		this.cmdName = cmdName;
		this.helpText = helpText;

		Class<?>[] c = {clazz};
		classes = c;
	}
	
	private StockGameCommandType(String cmdName, String helpText, Class<?> clazz1, Class<?> clazz2, Class<?> clazz3){
		this.cmdName = cmdName;
		this.helpText = helpText;
		
		Class<?>[] c = {clazz1,clazz2,clazz3};
		classes = c;
	}
	
	public String getName(){
		return cmdName;
	}
	
	public String getHelpText(){
		return helpText;
	}
	
	public Class<?>[] getParamTypes(){
		return classes;
	}
}
