package prog.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import prog.exception.InvalidArgumentException;
import prog.exception.NotEnoughArgumentsException;
import prog.exception.ObjectNotFoundException;
import prog.interfaces.CommandTypeInfo;

public class CommandScanner {
	
	BufferedReader reader;
	List<CommandTypeInfo> cmdTypeInfo;
	List<Object> varArgs = new ArrayList<Object>();
	Iterator<String> argsIterator;
	String commandLine;

	public CommandScanner(ArrayList<CommandTypeInfo> cmdTypeInfo, BufferedReader reader){
		this.cmdTypeInfo = cmdTypeInfo;
		this.reader = reader;
	}
	
	public CommandScanner(ArrayList<CommandTypeInfo> cmdTypeInfo){
		this.cmdTypeInfo = cmdTypeInfo;
	}
	
	public CommandDescriptor commandLine2CommandDescriptor(CommandDescriptor container, String commandLine){
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(commandLine.split(" ")));
		
		//Remove command name from args
		String cmdName = args.remove(0);
		argsIterator = args.iterator();
		
		CommandTypeInfo found = cmdTypeInfo
				.stream()
				.filter( cmd -> cmd.getName().equals(cmdName) )
				.findAny()
				.orElseThrow(() -> new ObjectNotFoundException("Command not found"));
		
		List<Class<?>> methodParameters = new ArrayList<Class<?>>( Arrays.asList(found.getParamTypes()) );
		List<Object> castedParameters = new ArrayList<Object>();
		
		methodParameters.forEach( p -> castedParameters.add( getCasted(p, argsIterator.hasNext() ? argsIterator.next() : null) ) );

		container.setParams(castedParameters.toArray(new Object[0]));
		container.setCommandType(found);
		
		return container;
	}
	
	public CommandDescriptor commandLine2CommandDescriptor(CommandDescriptor container) throws IOException
	{
		
		ArrayList<String> args = new ArrayList<String>(Arrays.asList(reader.readLine().split(" ")));
		
		// remove command name from args
		String cmdName = args.remove(0);
		
		argsIterator = args.iterator();
		
		CommandTypeInfo found = cmdTypeInfo
					.stream()
					.filter( cmd -> cmd.getName().equals(cmdName) )
					.findAny()
					.orElseThrow(() -> new ObjectNotFoundException("Command not found"));		

		
		List<Class<?>> methodParameters = new ArrayList<Class<?>>( Arrays.asList(found.getParamTypes()) );
	
		List<Object> castedParameters = new ArrayList<Object>();
		
			
		methodParameters.forEach( p -> castedParameters.add( getCasted(p, argsIterator.hasNext() ? argsIterator.next() : null) ) );
		

		container.setParams(castedParameters.toArray(new Object[0]));
		
		container.setCommandType(found);
	
		
		return container;
			
	}
	
	private Object getCasted( Class<?> parameter, String value )
	{
		// if parameter = varargs, join remaining args into array
		if (parameter.isArray())
		{
			varArgs.add(value);
			argsIterator.forEachRemaining( varArgs::add );
			return varArgs.toArray(new String[0]);
		}
		if (value == null)
			throw new NotEnoughArgumentsException("Not enough arguments.");
		Object result = null;
		try{
			//Try to cast the users arguments based on the list of parameter types
			result = parameter.cast(value);
		}catch(ClassCastException e){
			//If we try to cast a primitive time, a ClassCastException will be thrown 
			try{

				//Basic types have to be parsed, not casted
				switch(parameter.toString()){
				case "int":
					result = Integer.parseInt(value);
					break;
				case "float":
					result = Float.parseFloat(value);
					break;
				case "double":
					result = Double.parseDouble(value);
					break;
				case "long":
					result = Long.parseLong(value);
					break;
				case "short":
					result = Short.parseShort(value);
					break;
				case "byte":
					result = Byte.parseByte(value);
					break;
				case "boolean":
					result = Boolean.parseBoolean(value);
					break;
				}
			//If the string is in a wrong format, this exception will be thrown Example: Integer.parseInt("2.2");
			}catch(NumberFormatException ex){
				throw new InvalidArgumentException("Argument " + value + " is not in the right format");
			}
		}
		return result;
	}

}
