package com.example.LuanVanTotNghiep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.LuanVanTotNghiep"})
public class LuanVanTotNghiepApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuanVanTotNghiepApplication.class, args);
	}

}
