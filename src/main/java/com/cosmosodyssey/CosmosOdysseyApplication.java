package com.cosmosodyssey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CosmosOdysseyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmosOdysseyApplication.class, args);
	}

}

