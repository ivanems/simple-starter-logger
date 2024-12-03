package ru.ivanems.logger.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ivanems.logger.aspect.LogAspect;

@Configuration
@EnableConfigurationProperties(LoggerProps.class)
public class LoggerStarterConfiguration {

    private final LoggerProps loggerProps;

    public LoggerStarterConfiguration(LoggerProps loggerProps) {
        this.loggerProps = loggerProps;
    }

    @Bean
    @ConditionalOnProperty(prefix = "simple-logger", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LogAspect logAspect() {
        return new LogAspect(loggerProps);
    }

}
