package com.arvinsichuan.orange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Project Orange-public
 *
 * @Author: arvinsc
 * <p>
 * Date: 22-Apr-18
 * <p>
 * Package: com.arvinsichuan.orange
 */
@SpringBootApplication
public class OrangeApiStarter {


    /**
     *  TODO, Remove when production
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:4200");
            }
        };
    }

    public static void main(String[] args){
        SpringApplication.run(OrangeApiStarter.class);
    }


}
