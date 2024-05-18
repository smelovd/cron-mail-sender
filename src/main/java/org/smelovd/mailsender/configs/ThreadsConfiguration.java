package org.smelovd.mailsender.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

@Configuration
public class ThreadsConfiguration {

    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPool = new ThreadPoolTaskScheduler();
        threadPool.setPoolSize(Runtime.getRuntime().availableProcessors());
        threadPool.setThreadNamePrefix("threadPoolTaskScheduler");

        return threadPool;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor asyncMailThreadPool() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(6);
        threadPool.setMaxPoolSize(12);
        threadPool.setThreadNamePrefix("mailSender::");
        threadPool.initialize();
        return threadPool;
    }

    @Bean(name = "threadPoolTaskExecutorScheduler")
    public Executor asyncSchedulerThreadPool() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(4);
        threadPool.setMaxPoolSize(8);
        threadPool.setThreadNamePrefix("scheduler::");
        threadPool.initialize();
        return threadPool;
    }
}
