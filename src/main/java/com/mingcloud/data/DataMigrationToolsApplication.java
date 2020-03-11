package com.mingcloud.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.mingcloud.data.mapper")
@MapperScan(basePackages = "com.mingcloud.data.mapper.master")
@MapperScan(basePackages = "com.mingcloud.data.mapper.slave")
public class DataMigrationToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataMigrationToolsApplication.class, args);
	}

}
