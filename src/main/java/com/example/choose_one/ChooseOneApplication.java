package com.example.choose_one;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class ChooseOneApplication implements CommandLineRunner {
	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(ChooseOneApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("현재 DB 연결 정보: " + dataSource.getConnection().getMetaData().getURL());
	}

}
