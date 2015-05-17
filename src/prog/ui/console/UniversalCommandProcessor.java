package prog.ui.console;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import prog.interfaces.CommandTypeInfo;
import prog.ui.*;
import prog.exception.GameException;
import prog.exception.GameRuntimeException;
import prog.exception.ObjectNotFoundException;

public class UniversalCommandProcessor {
	private Object target;
	private Class<?> interf;
	private ArrayList<CommandTypeInfo> commandTypes;
	private BufferedReader reader;
	private PrintWriter writer;

	public UniversalCommandProcessor(Object target, Class<?> interf){
		this.target = target;
		this.interf = interf;
		writer = new PrintWriter(System.out);
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//Help command, always available
	@AsCommand(commandName = "help", description = "* list all commands")
	public void help(){
		String s = "";
		
		//Get help text of all registered commands
		for(CommandTypeInfo commandType : commandTypes){
			s += (commandType.getName() + " " + commandType.getHelpText() + "\n");
		}
		
		writer.print(s);
		process();
	}
	
	//Exit command, always available
	@AsCommand(commandName = "exit", description = "* exit program")
	public void exit(){
		writer.print("Bye");
		writer.flush();
		System.exit(0);
	}
	
	//Generates a list of CommandTypeInfo Objects from a target (for the methods) and a source class
	private ArrayList<CommandTypeInfo> generateCommandTypeInfoObjectsFromSource(Object target, Class<?> source){
		//Get all methods declared by the source
		Method[] methods = source.getDeclaredMethods();
		//Create a buffer that is large enough to theoretically save as many commandTypeInfos as there are defined methods

		ArrayList<CommandTypeInfo> cmdTypeInfos = new ArrayList<CommandTypeInfo>();
		

		//Get all methods
		for(Method method : methods){
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
	
	public void process(){
		//Generate type info before entering the main loop
		generateCommandTypeInfoObjects();
		//Initialize command scanner
		CommandScanner commandScanner = new CommandScanner(commandTypes, reader);
		
		//Main loop
		do{
			writer.flush();
			Object result = null;
			
			try{
				//Create container (information about the used command is stored here)
				Command command = new Command();
				commandScanner.commandLine2CommandDescriptor(command);
			
				result = null;
			
			
				result = command.execute();
			}catch (GameRuntimeException e) {
				//Exceptions thrown within the commandLine2CommandDescriptor method
				handleGameRuntimeException(e);
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}catch (IllegalArgumentException e){
				e.printStackTrace();
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
					handleGameException(ex);
				}catch(GameRuntimeException ex){
					handleGameRuntimeException(ex);
				};
			};
			
			//Print result
			if(result != null){
				writer.println(result);
			}

		}while(true);
	}

	//All GameRuntimeExceptions are handled here to make errors look better on the console
	private void handleGameRuntimeException(GameRuntimeException e) {
		writer.println(e.getMessage());
		//Restart main loop
		process();
	}

	//All GameExceptions are handled here to make errors look better on the console
	private void handleGameException(GameException e) {
		writer.println(e.getMessage());
		//Restart main loop
		process();
	}
}
