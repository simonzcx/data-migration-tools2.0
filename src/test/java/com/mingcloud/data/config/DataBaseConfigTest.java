package com.mingcloud.data.config;

import com.mingcloud.data.DataMigrationToolsApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;

@Slf4j
public class DataBaseConfigTest extends DataMigrationToolsApplicationTests {
    @Resource
    private DataBaseConfig dataBaseConfig;

    @Test
    public void test(){
        System.out.println("masterDataBase:"+DataBaseConfig.getMasterDataBase());
        System.out.println("slaveDataBase:"+DataBaseConfig.getSlaveDataBase());
        System.out.println("recordSqlTable:"+DataBaseConfig.getRecordSqlTable());
        System.out.println("FileName:"+DataBaseConfig.getFileName());
        System.out.println("FilePath:"+DataBaseConfig.getFilePath());
    }


}
