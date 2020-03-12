package com.mingcloud.data.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class DataBaseConfig  {

    private static String masterDataBaseUrl;
    @Getter
    private static String masterDataBase;

    private static String slaveDataBaseUrl;

    @Getter
    private static String slaveDataBase;

    @Getter
    private static String fileName;

    @Getter
    private static String filePath;

    @Getter
    private static String recordSqlTable;

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    public void setMasterDataBaseUrl(String masterDataBaseUrl) {
        DataBaseConfig.masterDataBaseUrl = masterDataBaseUrl;
        DataBaseConfig.masterDataBase = masterDataBaseUrl.substring(masterDataBaseUrl.lastIndexOf("/") + 1, masterDataBaseUrl.lastIndexOf("?"));
    }

    @Value("${spring.datasource.dynamic.datasource.slave.url}")
    public void setSlaveDataBaseUrl(String slaveDataBaseUrl) {
        DataBaseConfig.slaveDataBaseUrl = slaveDataBaseUrl;
        DataBaseConfig.slaveDataBase = slaveDataBaseUrl.substring(slaveDataBaseUrl.lastIndexOf("/") + 1, slaveDataBaseUrl.lastIndexOf("?"));
    }

    @Value("${application.fileName}")
    public void setFileName(String fileName) {
        DataBaseConfig.fileName = fileName;
    }
    @Value("${application.filePath}")
    public void setFilePath(String filePath) {
        DataBaseConfig.filePath = filePath;
    }
    @Value("${application.recordSqlTable}")
    public void setRecordSqlTable(String recordSqlTable) {
        DataBaseConfig.recordSqlTable = recordSqlTable;
    }

}
