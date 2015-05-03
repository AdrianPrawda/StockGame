package prog.ui;

import java.io.BufferedReader;
import java.io.IOException;

import prog.interfaces.CommandTypeInfo;
import prog.ui.CommandDescriptor;
import prog.exception.ObjectNotFoundException;
import prog.exception.NotEnoughArgumentsException;
import prog.exception.InvalidArgumentException;

public class CommandScanner {
	BufferedReader reader;
	CommandTypeInfo[] cmdTypeInfo;

	public CommandScanner(CommandTypeInfo[] cmdTypeInfo, BufferedReader reader){
		this.cmdTypeInfo = cmdTypeInfo;
		this.reader = reader;
	}
	
	public CommandDescriptor commandLine2CommandDescriptor(CommandDescriptor container){
		String line = "";
		
		try{
			line = reader.readLine();
		}catch(IOException e){
			//IO Error occurred
		}
		
		//First position is always the command identifier
		String args[] = line.split(" ");
		
		Class<?>[] paramType = null;
		CommandTypeInfo cmdt = null;
		
		//Find command in command type info objects
		for(int i=0; i<cmdTypeInfo.length; i++){
			System.out.flush();
			if(cmdTypeInfo[i].getName().equals(args[0])){
				//Command name found
				paramType = cmdTypeInfo[i].getParamTypes();
				cmdt = cmdTypeInfo[i];
			}
		}
		
		if(paramType == null || cmdt == null){
			//Command not found! (LOG!!)
			throw new ObjectNotFoundException("Command not found");
		}
		
		container.setCommandType(cmdt);
		
		Object[] castParams = new Object[paramType.length];
		int j = 0;
		
		if(paramType.length == 0 || paramType[0] == null){
			Class<?>[] p = {null};
			paramType = p;
			
			container.setParams(castParams);
			return container;
		}
		
		if(paramType.length+1 > args.length){
			//Not enough arguments!
			throw new NotEnoughArgumentsException("Invalid number of arguments. Expected " + paramType.length + " got " + (args.length-1));
		}
		
		for(Class<?> element : paramType){
			Object obj = null;
			
			try{
				obj = element.cast(args[j+1]);
			}catch(ClassCastException e){
				try{
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
				}catch(NumberFormatException ex){
					throw new InvalidArgumentException("Argument " + args[j+1] + " is not in the right format");
				}
			}
			castParams[j] = obj;
			j += 1;
		}
		
		container.setParams(castParams);
		
		return container;
	}
}
