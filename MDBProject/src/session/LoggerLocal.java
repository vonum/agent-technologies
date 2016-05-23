package session;

import javax.ejb.Local;

@Local
public interface LoggerLocal {

	public void log(String text);
}
