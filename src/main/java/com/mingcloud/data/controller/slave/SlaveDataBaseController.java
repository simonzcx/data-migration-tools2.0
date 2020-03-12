package com.mingcloud.data.controller.slave;


import com.alibaba.fastjson.JSONObject;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.service.slave.SlaveDataBaseService;
import com.mingcloud.data.service.slave.SlaveTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/slave")
public class SlaveDataBaseController {

    @Autowired
    private SlaveDataBaseService slaveDataBaseService;

    @Autowired
    private SlaveTableService slaveTableService;

    private String slaveDataBaseName;

    @RequestMapping(value = "/findAllTableName",method = RequestMethod.POST)
    public ServiceResponse<List<DataBaseEntity>> findAllTableName(){
        slaveDataBaseName = DataBaseConfig.getSlaveDataBase();
        List<DataBaseEntity> list = null;
        DataBaseEntity entity = new DataBaseEntity();
        entity.setDataBaseName(slaveDataBaseName);
        list = slaveDataBaseService.findAllTableName(entity);
        return ServiceResponse.ok(list);
    }

    @RequestMapping(value = "/findTableStructure",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ServiceResponse<List<TableEntity>> findTableStructure(@RequestBody JSONObject jsonParam){
        slaveDataBaseName = DataBaseConfig.getSlaveDataBase();
        List<TableEntity> list = null;
        //String tableName = "\"" + jsonParam.getString("tableName") + "\"";
        String tableName = jsonParam.getString("tableName");
        DataBaseEntity entity = new DataBaseEntity(slaveDataBaseName,tableName);
        list = slaveTableService.findTableStructure(entity);
        return ServiceResponse.ok(list);
    }


}
