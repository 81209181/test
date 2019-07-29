package com.hkt.btu.noc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileNotFoundException;

@MapperScan("com.hkt.btu.noc.core.dao.mapper")
@ComponentScan("com.hkt.*")
@SpringBootApplication//exclude = DataSourceAutoConfiguration.class)
public class NocApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NocApplication.class);
    }

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(NocApplication.class);
    }
}
