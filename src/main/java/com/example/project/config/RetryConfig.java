package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;


@EnableRetry
@Configuration
public class RetryConfig {
    //재처리를 위한 config 클래스 - 어노테이션을 이용한 방법을 사용할 예정



//    빈으로 등록해서 사용하는 방법
//    @Bean
//    public RestTemplate restTemplate(){
//        return new RestTemplate();
//    }
}
