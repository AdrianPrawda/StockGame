package prog.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import prog.exception.GameException;
import prog.exception.GameRuntimeException;
import prog.interfaces.CommandTypeInfo;

public class CommandProcessor {
	private Object target;
	private Class<?> interf;
	private ArrayList<CommandTypeInfo> commandTypes;
	private ResourceBundle bundle;
	private CommandScanner commandScanner;
	
	public CommandProcessor(Object target, Class<?> interf){
		this.target = target;
		this.interf = interf;
		
		//Generate type info
		generateCommandTypeInfoObjects();
		//Initialize command scanner
		this.commandScanner = commandScanner = new CommandScanner(commandTypes);
	}
	
	public void setResourceBundle(ResourceBundle bundle){
		this.bundle = bundle;
	}
	
	//Help command, always available
	@AsCommand(commandName = "help", description = "* list all commands")
	public String help(){
		
		//Get help text of all registered commands
		String s = commandTypes
				.stream()
				.map(c -> c.getName() + " " + c.getHelpText())
				.collect(Collectors.joining("\n"));
		
		return s;
	}
	
	//Exit command, always available
	@AsCommand(commandName = "exit", description = "* exit program")
	public String exit(){
		return "0";
	}
	
	//Generates a list of CommandTypeInfo Objects from a target (for the methods) and a source class
	private ArrayList<CommandTypeInfo> generateCommandTypeInfoObjectsFromSource(Object target, Class<?> source){

		ArrayList<CommandTypeInfo> cmdTypeInfos = new ArrayList<CommandTypeInfo>();

		//Get all methods
		for( Method method : source.getDeclaredMethods() ){
			//Check if the method has an AsCommand annotation. If not the value of a will be null
			AsCommand a = method.getAnnotation(AsCommand.class);
			if(a != null){
				//Command found!
				//Generate new CommandType
				CommandType c = new CommandType(a.commandName(),a.description(),method,target,method.getParameterTypes());
				cmdTypeInfos.add(c);
			
			}
		}
			
		return cmdTypeInfos;
	}
	
	private void generateCommandTypeInfoObjects(){
		//Generate the type info of all always available commands (In short: all commands defined in the UniversalCommandProcessor class)
		ArrayList<CommandTypeInfo> std = generateCommandTypeInfoObjectsFromSource(this, this.getClass());
		//Generate type info
		ArrayList<CommandTypeInfo> add = generateCommandTypeInfoObjectsFromSource(target, interf);
		
		std.addAll(add);

		
		//Save resulting array
		commandTypes = std;
	}
	
	public Object process(String commandLine){
		Object result = null;
		
		try{
			//Create container (information about the used command is stored here)
			Command command = new Command();
			commandScanner.commandLine2CommandDescriptor(command, commandLine);
		
			result = command.execute();
		}catch (GameRuntimeException e) {
			//Exceptions thrown within the commandLine2CommandDescriptor method
			return e;
		}catch (IllegalAccessException e){
			e.printStackTrace();
		}catch (IllegalArgumentException e){
			e.printStackTrace();
			throw e;
		}catch (InvocationTargetException e){
			//Identify the cause of the InvocationTargetException and re-throw it
			//Thrown if the invoked method throws an exception
			try{
				//Thrown if the cause was a GameException
				if(e.getCause() instanceof GameException){
					throw new GameException(e.getCause().getMessage());
				//Thrown if the cause was a GameRuntimeException
				}else if(e.getCause() instanceof GameRuntimeException){
					throw new GameRuntimeException(e.getCause().getMessage());
				}
			}catch(GameException ex){
				return ex;
			}catch(GameRuntimeException ex){
				return ex;
			};
		};
		
		return result;
	}
	
}
