package me.noteme.headhunting.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "emailSendExecutor")
    public ThreadPoolTaskExecutor emailSendExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(200);

        executor.setThreadNamePrefix("EmailSendExecutor-");
        executor.initialize();

        return executor;
    }
}
