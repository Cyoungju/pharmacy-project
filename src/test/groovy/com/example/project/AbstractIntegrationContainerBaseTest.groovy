package com.example.project

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.GenericContainer
import spock.lang.Specification

// 통합 테스트 환경에서 테스트를 진행할수 있게 클래스 생성
// 해당 클래스를 상속받으면 통합 테스트 환경으로 테스트 코드를 작성할수 있음
@SpringBootTest
abstract class AbstractIntegrationContainerBaseTest extends Specification{
    // 싱글톤
    static final GenericContainer MY_REDIS_CONTAINER

    static {
        //레디스는 직접 연결 해줘야함
        MY_REDIS_CONTAINER = new GenericContainer<>("redis:6")
            .withExposedPorts(6379) // 도커에서 익스포즈한 포트

        MY_REDIS_CONTAINER.start()

        System.setProperty("spring.redis.host",MY_REDIS_CONTAINER.getHost())
        System.setProperty("spring.redis.port",MY_REDIS_CONTAINER.getMappedPort(6379).toString())
        //스프링부트에게 6379와 랜덤하게 맵핑된 포트를 전달
    }
}
