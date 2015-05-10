package formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class InvocationFormatter extends Formatter 
{

	@Override
	public String format(LogRecord record) 
	{
		String s = "[" + record.getLevel() + "]\t\t";
		s += record.getMessage();
		s += "\r\n";
		return s;
	}

}
