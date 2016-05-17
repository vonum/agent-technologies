package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.AgentType;

public final class Utility {

	public static List<AgentType> readAgentTypesFromFile(String file)
	{
		List<AgentType> types = new ArrayList<AgentType>();
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
		    	
		    	types.add(type);
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
