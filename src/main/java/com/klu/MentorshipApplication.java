package com.klu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MentorshipApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentorshipApplication.class, args);
	}

}
