package com.mingcloud.data.mapper.master;

import com.mingcloud.data.DataMigrationToolsApplicationTests;
import com.mingcloud.data.entity.DataBaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class MasterDataBaseMapperTest  extends DataMigrationToolsApplicationTests {
    @Resource
    private MasterDataBaseMapper masterDataBaseMapper;

    @Test
    public void testSelectAllTableName(){
        String databaseName = new String("geely1.0");
        databaseName = "\'" + databaseName + "\'";
        DataBaseEntity entity = new DataBaseEntity();
        entity.setDataBaseName(databaseName);
        List<DataBaseEntity> entityList = masterDataBaseMapper.selectAllTableName(entity);
        for (DataBaseEntity entity1 : entityList) {
            System.out.println("tableName:"+entity1.getTableName());
        }
    }

}
