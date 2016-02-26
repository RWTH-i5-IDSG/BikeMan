package de.rwth.idsg.bikeman.config;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.FopFactoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;

@Configuration
public class PdfConfiguration implements EnvironmentAware {
    private static final String ENV_SPRING_PDF = "spring.pdf.";
    private static final String PROP_BASEURI = "baseuri";
    private static final String PROP_PAGE_HEIGHT = "page_height";
    private static final String PROP_PAGE_WIDTH = "page_width";
    private static final String PROP_PAGE_RESOLUTION = "resolution";
    private static final String PROP_STRICT = "strict";

    private RelaxedPropertyResolver propertyResolver;

    private final Logger log = LoggerFactory.getLogger(PdfConfiguration.class);

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_SPRING_PDF);
    }

    @Bean
    public FopFactory fopFactory() throws Exception {
        String baseUri = propertyResolver.getProperty(PROP_BASEURI, "resources/pdf/");
        String pageHeight = propertyResolver.getProperty(PROP_PAGE_HEIGHT, "297mm");
        String pageWidth = propertyResolver.getProperty(PROP_PAGE_WIDTH, "210mm");
        Float resolution = propertyResolver.getProperty(PROP_PAGE_RESOLUTION, Float.class, 96.0f);
        Boolean strict = propertyResolver.getProperty(PROP_STRICT, Boolean.class, true);

        try {
            FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(new File(baseUri).toURI());
            fopFactoryBuilder = fopFactoryBuilder
                .setPageHeight(pageHeight)
                .setPageWidth(pageWidth)
                .setStrictUserConfigValidation(strict)
                .setTargetResolution(resolution)
                .setStrictFOValidation(strict);

            return fopFactoryBuilder.build();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
