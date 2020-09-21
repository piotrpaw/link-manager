package com.ppawl.linkmanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
@Slf4j
public class LinkManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkManagerApplication.class, args);
    }

    @Bean(name = "processExecutor")
    public TaskExecutor processExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(1);
        threadPoolTaskExecutor.setQueueCapacity(600);
        threadPoolTaskExecutor.afterPropertiesSet();
        threadPoolTaskExecutor.setThreadNamePrefix("Link Queue");

        log.info("ThreadPoolTaskExecutor set");
        return threadPoolTaskExecutor;
    }
}

