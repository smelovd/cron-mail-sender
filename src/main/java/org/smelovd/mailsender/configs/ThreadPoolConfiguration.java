package org.smelovd.mailsender.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfiguration {

    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        threadPool.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPool.setThreadNamePrefix("scheduler::");

        return threadPool;
    }

    @Bean(name = "threadPoolTaskExecutorScheduler")
    public Executor asyncSchedulerThreadPool() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(6);
        threadPool.setMaxPoolSize(12);
        threadPool.setThreadNamePrefix("schedulerTask::");
        threadPool.setQueueCapacity(512);
        threadPool.initialize();

        return threadPool;
    }
}
