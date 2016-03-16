package fr.jayblanc.pigrows;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

public class PiGrowsApplication extends ResourceConfig {

    public PiGrowsApplication() {
        packages("fr.jayblanc.pigrows.resources");
        register(LoggingFilter.class);
        register(FreemarkerMvcFeature.class);
    }
}