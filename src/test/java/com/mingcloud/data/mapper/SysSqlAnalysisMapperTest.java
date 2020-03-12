package com.mingcloud.data.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mingcloud.data.DataMigrationToolsApplicationTests;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;

import com.mingcloud.data.service.SysSqlAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SysSqlAnalysisMapperTest extends DataMigrationToolsApplicationTests {

    @Resource
    private SysSqlAnalysisMapper sysSqlAnalysisMapper;

    @Resource
    private SysSqlAnalysisService sysSqlAnalysisService;


    //private DataBaseConfig dataBaseConfig;

    private String recordSqlTable;

    private String masterDataBase;

    private String slaveDataBase;


    @Test
    @DS("master")
    public void SysSqlAnalysisMapperTest() throws IOException {
        DataBaseEntity entity = new DataBaseEntity();
        String tableName = new String("app");
        tableName = "\"" + tableName + "\"";
        entity.setTableName(tableName);

        String databaseName = new String("geely1.0");
        databaseName = "\'" + databaseName + "\'";
        entity.setDataBaseName(databaseName);
        System.out.println("tableName:" + tableName + "   databaseName:" + databaseName);
        int existTable = sysSqlAnalysisMapper.existTable(entity);
        System.out.println("existTable:" + existTable);
    }

    @Test
    @DS("master")
    public void SysSqlAnalysisMapperTestExist() throws IOException {
        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(recordSqlTable);
        entity.setDataBaseName(masterDataBase);
        int existTable = sysSqlAnalysisMapper.existTable(entity);
        System.out.println("existTable:" + existTable +"recordSqlTable:"+recordSqlTable);
    }

    @Test
    @DS("master")
    public void SysSqlAnalysisMapperTestSelectDataInto() throws IOException {
        TableDto dto = new TableDto();
        Map<String, Object> dataBaseMap = new HashMap<>();

        String newDataBase = "`"+ masterDataBase +"`";
        String oldDataBase = "`"+ slaveDataBase +"`";

        dataBaseMap.put("newDataBase", newDataBase);
        dataBaseMap.put("oldDataBase", oldDataBase);

        Map<String, Object> tableMap = new HashMap<>();
        dto.setNewTable("t_user");//2.0
        dto.setOldTable("t_user");//1.0

        dto.columnMap.put("id", "id");
        String name = "`" + "name" + "`";
        dto.columnMap.put("name", name);
        dto.columnMap.put("age", "age");


        tableMap.put("newTable", dto.getNewTable());
        tableMap.put("oldTable", dto.getOldTable());

        Map<String, Object> map = new HashMap<>();
        map.put("dataBaseMap", dataBaseMap);
        map.put("tableMap", tableMap);
        map.put("columnMap", dto.columnMap);

        sysSqlAnalysisMapper.selectDataInto(map);


        Set<Map.Entry<String, Object>> set = dto.columnMap.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key:" + key + ",value:" + value);
        }
    }

    @Test
    public void testInit(){
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        System.out.println("recordSqlTable:"+recordSqlTable);
        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName("aaa");
        entity.setDataBaseName(masterDataBase);
        sysSqlAnalysisMapper.createTable(entity);
    }
    @Test
    public void testServiceInit(){
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        masterDataBase = DataBaseConfig.getMasterDataBase();
        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(recordSqlTable);
        entity.setDataBaseName(masterDataBase);
        sysSqlAnalysisService.sysSqlAnalysisInit(entity);
    }



}
