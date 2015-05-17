package prog.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import prog.interfaces.CommandTypeInfo;
import prog.ui.CommandDescriptor;
import prog.exception.ObjectNotFoundException;
import prog.exception.NotEnoughArgumentsException;
import prog.exception.InvalidArgumentException;

public class CommandScanner {
	BufferedReader reader;
	ArrayList<CommandTypeInfo> cmdTypeInfo;

	public CommandScanner(ArrayList<CommandTypeInfo> cmdTypeInfo, BufferedReader reader){
		this.cmdTypeInfo = cmdTypeInfo;
		this.reader = reader;
	}
	
	//Convert the users input to a more useful one
	public CommandDescriptor commandLine2CommandDescriptor(CommandDescriptor container){
		//The users input
		String line = "";
		
		try{
			line = reader.readLine();
		}catch(IOException e){
			//IO Error occurred
		}
		
		//First position is always the command identifier (args[0] = command name)
		String args[] = line.split(" ");
		
		//Buffer for an array of parameter types
		Class<?>[] paramType = null;
		//Buffer for the command type information
		CommandTypeInfo cmdt = null;
		
		//Find command in the list of command type info objects
		for( CommandTypeInfo current : cmdTypeInfo ){
			//Search for the object representing the command from the user
			if(current.getName().equals(args[0])){
				//Command found
				cmdt = current;
				paramType = cmdt.getParamTypes();
			}
		}
		
		//If the command from the user is not defined paramType and cmdt will be null
		if(paramType == null || cmdt == null){
			//Command not found! (LOG!!)
			throw new ObjectNotFoundException("Command not found");
		}
		
		//Save the command type info representing the users command
		container.setCommandType(cmdt);
		
		//Object buffer
		Object[] castParams = new Object[paramType.length];
		int j = 0;
		
		//If the length of the paramType array is 0 or if the first entry is null, the methods has no arguments (example: method())
		if(paramType.length == 0 || paramType[0] == null){
			//Create empty class<?> array
			Class<?>[] p = {null};
			paramType = p;
			
			//And set is as the methods parameter list
			container.setParams(castParams);
			return container;
		}
		
		//Check if the user entered at least as many arguments as required by the method
		if(paramType.length+1 > args.length){
			//Not enough arguments!
			throw new NotEnoughArgumentsException("Invalid number of arguments. Expected " + paramType.length + " got " + (args.length-1));
		}
		
		//Generate required objects
		for(Class<?> element : paramType){
			Object obj = null;
			
			try{
				//Try to cast the users arguments based on the list of parameter types
				obj = element.cast(args[j+1]);
			}catch(ClassCastException e){
				//If we try to cast a primitive time, a ClassCastException will be thrown 
				try{
					//Basic types have to be parsed, not casted
					switch(element.toString()){
					case "int":
						obj = Integer.parseInt(args[j+1]);
						break;
					case "float":
						obj = Float.parseFloat(args[j+1]);
						break;
					case "double":
						obj = Double.parseDouble(args[j+1]);
						break;
					case "long":
						obj = Long.parseLong(args[j+1]);
						break;
					case "short":
						obj = Short.parseShort(args[j+1]);
						break;
					case "byte":
						obj = Byte.parseByte(args[j+1]);
						break;
					case "boolean":
						obj = Boolean.parseBoolean(args[j+1]);
						break;
					}
				//If the string is in a wrong format, this exception will be thrown Example: Integer.parseInt("2.2");
				}catch(NumberFormatException ex){
					throw new InvalidArgumentException("Argument " + args[j+1] + " is not in the right format");
				}
			}
			//Fill object buffer
			castParams[j] = obj;
			j += 1;
		}
		
		//Save objects
		container.setParams(castParams);
		
		return container;
	}
}
