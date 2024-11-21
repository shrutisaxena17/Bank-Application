package com.example.BankApplication;

import com.example.BankApplication.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BankApplication {

	private static final Logger logger = LoggerFactory.getLogger(BankApplication.class);


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BankApplication.class, args);
		AppConfig appConfig = context.getBean(AppConfig.class);
		appConfig.print();
		logger.info("This is an INFO message from {}",BankApplication.class.getSimpleName());
		logger.warn("This is a WARN message");
		logger.error("This is an ERROR message");
	}

}
