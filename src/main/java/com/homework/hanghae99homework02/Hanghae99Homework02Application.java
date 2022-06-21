package com.homework.hanghae99homework02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing //?? 어떻게 바꿔야하지? 에러가능성 TODO
@SpringBootApplication
@PropertySource("application-dev.properties")
public class Hanghae99Homework02Application {

    public static void main(String[] args) {
        SpringApplication.run(Hanghae99Homework02Application.class, args);
    }
    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
