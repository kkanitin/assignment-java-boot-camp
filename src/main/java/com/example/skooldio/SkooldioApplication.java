package com.example.skooldio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class SkooldioApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkooldioApplication.class, args);
	}

}
