package com.frost.bfriend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BfriendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BfriendApplication.class, args);
	}

}
