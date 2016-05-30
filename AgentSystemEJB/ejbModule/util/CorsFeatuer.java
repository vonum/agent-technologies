package util;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

@Provider
public class CorsFeatuer implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		// TODO Auto-generated method stub
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        context.register(corsFilter);
        
        return true;
	}

}
