package session;

import javax.ejb.Singleton;

@Singleton
public class LoggerBean implements LoggerLocal {
	int count = 0;
	@Override
	public void log(String text) {
		System.out.println("LOGGER BEAN " + (++count) +" LOGGED: " + text);
	}

}
