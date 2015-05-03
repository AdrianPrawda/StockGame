package prog.ui;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsCommand {
	String commandName();
	String description();
}
