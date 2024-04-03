package org.company.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolTaskSchedulerConfig {

    ThreadPoolTaskScheduler threadPoolTaskScheduler;
    public ThreadPoolTaskSchedulerConfig() {
        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(50);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return threadPoolTaskScheduler;
    }
}
