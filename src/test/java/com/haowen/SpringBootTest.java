package com.haowen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.haowen")
public class SpringBootTest {

	public static void main(String[] args) {
		System.out.println("");
		SpringApplication.run(SpringBootTest.class, args);
	} 
	  

	}
   