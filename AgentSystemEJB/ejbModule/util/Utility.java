package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import model.AgentType;

public final class Utility {

	public static Map<String, AgentType> readAgentTypesFromFile(String file)
	{
		Map<String, AgentType> types = new HashMap<String, AgentType>();
		String line;
		
		try (
		    InputStream fis = new FileInputStream(file);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		) {
		    while ((line = br.readLine()) != null) {
		        // Deal with the line
		    	System.out.println(line);
		    	String[] parts = line.split(":");
		    	AgentType type = new AgentType(parts[0], parts[1]);
		    	
		    	types.put(parts[0], type);
		    }
		    
		    return types;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

	}
	
	
}
