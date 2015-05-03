package prog.ui.console;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

import prog.interfaces.CommandTypeInfo;
import prog.ui.*;
import prog.exception.GameException;
import prog.exception.GameRuntimeException;
import prog.exception.ObjectNotFoundException;

public class UniversalCommandProcessor {
	private Object target;
	private Class<?> interf;
	private CommandTypeInfo[] commandTypes;
	private BufferedReader reader;
	private PrintWriter writer;
	
	private final String HELP = "help";
	private final String EXIT = "exit";

	public UniversalCommandProcessor(Object target, Class<?> interf){
		this.target = target;
		this.interf = interf;
		writer = new PrintWriter(System.out);
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	@AsCommand(commandName = "help", description = "* list all commands")
	public void help(){
		String s = "";
		
		for(CommandTypeInfo commandType : commandTypes){
			s += (commandType.getName() + " " + commandType.getHelpText() + "\n");
		}
		
		writer.print(s);
		process();
	}
	
	@AsCommand(commandName = "exit", description = "* exit program")
	public void exit(){
		writer.print("Bye");
		writer.flush();
		System.exit(0);
	}
	
	private CommandTypeInfo[] generateCommandTypeInfoObjectsFromSource(Object target, Class<?> source){
		Method[] methods = source.getDeclaredMethods();
		CommandTypeInfo[] cmdTypeInfos = new CommandTypeInfo[methods.length+2];
		
		int p=0;
		
		for(Method method : methods){
			AsCommand a = method.getAnnotation(AsCommand.class);
			if(a != null){
				//Command found!
//				System.out.println("Command Name: \"" + a.commandName() + "\" Description: \"" + a.description() + "\" Method: \"" + method.getName() + "\" Target: \"" + target.getClass() + "\" ParamTypes: \"" + method.getParameterCount() + "\"");
				CommandType c = new CommandType(a.commandName(),a.description(),method,target,method.getParameterTypes());
				cmdTypeInfos[p] = c;
				p += 1;
			}
		}
		
		CommandTypeInfo[] out = new CommandTypeInfo[p];
		for(int j=0; j<out.length; j++){
			out[j] = cmdTypeInfos[j];
		}
		
		return out;
	}
	
	private void generateCommandTypeInfoObjects(){
		CommandTypeInfo[] std = generateCommandTypeInfoObjectsFromSource(this, this.getClass());
		CommandTypeInfo[] add = generateCommandTypeInfoObjectsFromSource(target, interf);
		
		CommandTypeInfo[] out = new CommandTypeInfo[std.length + add.length];
		for(int i=0; i<std.length; i++){
			out[i] = std[i];
		}
		for(int j=0; j<add.length; j++){
			out[j+std.length] = add[j];
		}
		
		commandTypes = out;
	}
	
	public void process(){
		generateCommandTypeInfoObjects();
		
		CommandScanner commandScanner = new CommandScanner(commandTypes, reader);
		
		//Main loop
		do{
			writer.flush();
			Object result = null;
			
			try{
				Command command = new Command();
				commandScanner.commandLine2CommandDescriptor(command);
			
				writer.println("Executing command...");
				result = null;
			
			
				result = command.execute();
			}catch (GameRuntimeException e) {
				handleGameRuntimeException(e);
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}catch (IllegalArgumentException e){
				e.printStackTrace();
			}catch (InvocationTargetException e){
				//Identify the cause of the InvocationTargetException and re-throw it
				try{
					if(e.getCause() instanceof GameException){
						throw new GameException(e.getCause().getMessage());
					}else if(e.getCause() instanceof GameRuntimeException){
						throw new GameRuntimeException(e.getCause().getMessage());
					}
				}catch(GameException ex){
					handleGameException(ex);
				}catch(GameRuntimeException ex){
					handleGameRuntimeException(ex);
				};
			};
			
			if(result != null){
				writer.println(result);
			}

		}while(true);
	}

	private void handleGameRuntimeException(GameRuntimeException e) {
		writer.println(e.getMessage());
		process();
	}

	private void handleGameException(GameException e) {
		writer.println(e.getMessage());
		process();
	}
}
