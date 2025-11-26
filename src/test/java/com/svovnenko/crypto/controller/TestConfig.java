package com.svovnenko.crypto.controller;

import com.svovnenko.crypto.repository.CryptoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.svovnenko.crypto")
public class TestConfig {

    @Bean
    @Primary
    public CryptoRepository repository() {
        return mock(CryptoRepository.class);
    }
}
