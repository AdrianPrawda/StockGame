package prog.core;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import formatter.FileFormatter;
import formatter.InvocationFormatter;
import prog.interfaces.AccountManager;
import prog.ui.AsCommand;

public class AccountManagerProxy implements InvocationHandler  
{
	
	private static Logger logger = Logger.getLogger(AccountManagerProxy.class.getName());
	
    private AccountManager account;
    
    public AccountManagerProxy(AccountManager account) throws SecurityException, IOException  
    { 
    	this.account = account; 
  
    	ConsoleHandler consoleHandler = new ConsoleHandler();
    	FileHandler fileHandler = new FileHandler("session.txt");
    	logger.addHandler(consoleHandler);
    	logger.addHandler(fileHandler);
    	logger.setLevel(Level.ALL);
    	
    	consoleHandler.setLevel(Level.ALL);
    	fileHandler.setLevel(Level.ALL);
    	
    	consoleHandler.setFormatter(new InvocationFormatter());
    	fileHandler.setFormatter(new FileFormatter());
    
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable  {
    	
        AsCommand[] cmd = method.getAnnotationsByType(AsCommand.class);
        
        String log = "";
        
        // Falls Aufruf von User-Input stammt, logge dies explizit
        if (cmd.length > 0)
        {
		    log = cmd[0].commandName();
		    if (args != null)
		    	for (int i = 0; i < args.length; i++)
		    		log += " "+ args[i];
		    logger.finest(log);
        }
        
        log = "method: "+ method.getName();
        
        if (args != null)
        {
         log += "\targs:";
         for (int i = 0; i < args.length; i++) 
            log += " " + args[i];
        }
    
        Object result = null;
        try
        {
        	result = method.invoke(account, args);
        	
        	if (result != null)
        		log += "\tresult: "+ result;	
            
        }
        catch(InvocationTargetException e)  
        {
        	log += "\texception: " + e.getTargetException();
        	
        	throw e.getTargetException();
        }
        finally
        {
        	logger.fine(log);		
        }
        
        return result;
    }
}