package com.hkt.btu.sd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;

@MapperScan("com.hkt.btu.sd.core.dao.mapper")
@ComponentScan("com.hkt.*")
@SpringBootApplication//exclude = DataSourceAutoConfiguration.class)
public class SdApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SdApplication.class);
    }

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(SdApplication.class);
    }
}
