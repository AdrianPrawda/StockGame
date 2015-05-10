package formatter;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter 
{

	@Override
	public String format(LogRecord record) 
	{
		String s = "";
		if (record.getLevel() != Level.FINEST)
			s += "#";
		s += record.getMessage();
		s += "\r\n";
		return s;
	}

}
