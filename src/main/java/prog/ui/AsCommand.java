package prog.ui;

import java.lang.annotation.*;

//Annotation used to mark a method as a command

//Make sure annotation is available during runtime
@Retention(RetentionPolicy.RUNTIME)
//Makes sure that only methods can be marked as commands
@Target(ElementType.METHOD)
public @interface AsCommand {
	//A commands has a name
	String commandName();
	//And a description / help text
	String description();
}
