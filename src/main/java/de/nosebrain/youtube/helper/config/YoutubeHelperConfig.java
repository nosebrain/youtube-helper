package de.nosebrain.youtube.helper.config;

import de.nosebrain.youtube.helper.YoutubeHelperApplicationInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackageClasses = { YoutubeHelperApplicationInitializer.class })
public class YoutubeHelperConfig extends WebMvcConfigurationSupport {
  // noop
}
