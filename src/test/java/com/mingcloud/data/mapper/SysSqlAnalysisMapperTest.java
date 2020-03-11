package com.mingcloud.data.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mingcloud.data.DataMigrationToolsApplicationTests;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.service.master.MasterDataBaseService;
import com.mingcloud.data.service.slave.SlaveDataBaseService;
import com.mingcloud.data.service.slave.SlaveTableService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    private MasterDataBaseService masterDataBaseService;

    @Resource
    private SlaveDataBaseService slaveDataBaseService;


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
    public void SysSqlAnalysisMapperTestSelectDataInto() throws IOException {
        TableDto dto = new TableDto();
        Map<String, Object> dataBaseMap = new HashMap<>();
        dto.setNewDataBase(masterDataBaseService.selectDatabaseNameByMySQL());
        dto.setOldDataBase(slaveDataBaseService.selectDatabaseNameByMySQL());
        String newDataBase = "`"+ dto.getNewDataBase() +"`";
        String oldDataBase = "`"+ dto.getOldDataBase() +"`";

        dataBaseMap.put("newDataBase", newDataBase);
        dataBaseMap.put("oldDataBase", oldDataBase);

        Map<String, Object> tableMap = new HashMap<>();
        dto.setNewTable("t_user");//2.0
        dto.setOldTable("t_user");//1.0
        dto.map.put("id", "id");
        String name = "`" + "name" + "`";
        dto.map.put("name", name);
        dto.map.put("age", "age");


        tableMap.put("newTable", dto.getNewTable());
        tableMap.put("oldTable", dto.getOldTable());

        Map<String, Object> map = new HashMap<>();
        map.put("dataBaseMap", dataBaseMap);
        map.put("tableMap", tableMap);
        map.put("columnMap", dto.map);
        sysSqlAnalysisMapper.selectDataInto(map);


        Set<Map.Entry<String, Object>> set = dto.map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key:" + key + ",value:" + value);
        }
    }


}
