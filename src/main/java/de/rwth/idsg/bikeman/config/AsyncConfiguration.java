package de.rwth.idsg.bikeman.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import de.rwth.idsg.bikeman.async.ExceptionHandlingAsyncTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@EnableScheduling
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class AsyncConfiguration implements AsyncConfigurer, EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    private ScheduledThreadPoolExecutor executor;
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "async.");
    }

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(propertyResolver.getProperty("corePoolSize", Integer.class, 2));
        executor.setMaxPoolSize(propertyResolver.getProperty("maxPoolSize", Integer.class, 50));
        executor.setQueueCapacity(propertyResolver.getProperty("queueCapacity", Integer.class, 10000));
        executor.setThreadNamePrefix("bikeman-Executor-");
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    /**
     * TODO:
     * In an ideal world, we should converge getAsyncExecutor() and scheduledExecutorService(),
     * so that we have only one global executor service for the whole application
     */
    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("bikeman-Executor-Service-%d")
                                                                .build();

        executor = new ScheduledThreadPoolExecutor(5, threadFactory);
        return executor;
    }

    @PreDestroy
    public void shutDown() {
        if (executor != null) {
            gracefulShutDown(executor);
        }
    }

    private void gracefulShutDown(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            log.error("Termination interrupted", e);

        } finally {
            if (!executor.isTerminated()) {
                log.warn("Killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }
}
