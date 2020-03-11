package com.mingcloud.data.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DataBaseConfig {

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String masterDataBaseName;

    @Value("${spring.datasource.dynamic.datasource.slave.url}")
    private String slaveDataBaseName;

    @Value("${application.fileName}")
    private String fileName;

    @Value("${application.filePath}")
    private String filePath;

    @Value("${application.recordSqlTable}")
    private String recordSqlTable;


}
