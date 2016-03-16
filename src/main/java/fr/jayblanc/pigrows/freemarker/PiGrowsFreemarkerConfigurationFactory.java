package fr.jayblanc.pigrows.freemarker;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.glassfish.jersey.server.mvc.freemarker.FreemarkerDefaultConfigurationFactory;
import org.jvnet.hk2.annotations.Optional;

import freemarker.template.Configuration;

public class PiGrowsFreemarkerConfigurationFactory extends FreemarkerDefaultConfigurationFactory {

    @Inject
    public PiGrowsFreemarkerConfigurationFactory(@Optional ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public Configuration getConfiguration() {
        Configuration config = super.getConfiguration();
        config.setSharedVariable("formatsize", new FormatSizeMethod());
        return config;
    }

    
        
}
