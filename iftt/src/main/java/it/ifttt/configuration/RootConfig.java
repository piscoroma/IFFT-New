package it.ifttt.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages={"it.ifttt"})
@EnableScheduling
@PropertySource("classpath:application.properties")
public class RootConfig {

}
