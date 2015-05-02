package prog.ui.console;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import prog.core.AccountManagerImpl;
import prog.ui.CommandScanner;
import prog.core.enumerator.StockGameCommandType;
import prog.ui.CommandDescriptor;
import prog.exception.GameException;
import prog.exception.GameRuntimeException;
import prog.exception.ObjectNotFoundException;

public class StockGameCommandProcessor {
	private AccountManagerImpl manager;
	private BufferedReader reader;
	private PrintWriter writer;
	private StockGameCommandType commandType;
	
	public StockGameCommandProcessor(AccountManagerImpl accountManager){
		manager = accountManager;
		reader = new BufferedReader(new InputStreamReader(System.in));
		writer = new PrintWriter(System.out);
	}
	
	private void handleGameException(GameException e){
		writer.println(e.getMessage());
		process();
	}
	
	private void handleGameRuntimeException(GameRuntimeException e){
		writer.println(e.getMessage());
		process();
	}
	
	private void handleNoSuchMethodException(NoSuchMethodException e){
		writer.println(e.getMessage());
		process();
	}
	
	private void exit(){
		writer.print("Bye");
		writer.flush();
		System.exit(0);
	}
	
	private void help(){
		writer.println(commandType.getHelpText());
		process();
	}
	
	public void process(){
		CommandScanner scanner = new CommandScanner(StockGameCommandType.values(), reader);
		
			//Main loop
			do{
				writer.flush();
				CommandDescriptor commandDescriptor = new CommandDescriptor();
				
				try{
					commandDescriptor = scanner.commandLine2CommandDescriptor(commandDescriptor);
				}catch(GameRuntimeException e){
					writer.println("Executing command...");
					handleGameRuntimeException(e);
				}				
				
				commandType = (StockGameCommandType)commandDescriptor.getCommandType();
				writer.println("Executing command...");
				
				try{
					if(!commandType.getMethodName().equals("")){
						//Invoke methods from AccountManagerImpl
						Method m = manager.getClass().getDeclaredMethod(commandType.getMethodName(), commandType.getParamTypes());
						Object o = m.invoke(manager, commandDescriptor.getParams());
						
						if(o != null){
							writer.println(o);
							writer.flush();
						}
					}else{
						//Invoke methods from StockGameCommandProcessor
						Method m = this.getClass().getDeclaredMethod(commandType.getName());
						m.invoke(this);
					}
				}catch(NoSuchMethodException e){
					handleNoSuchMethodException(e);
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}catch(InvocationTargetException e){
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
					}
				}
				
				writer.println("Done");
				
			}while(true);	
	}
	
}
