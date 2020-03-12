package com.mingcloud.data.controller.master;


import com.alibaba.fastjson.JSONObject;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.service.master.MasterDataBaseService;
import com.mingcloud.data.service.master.MasterTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/master")
public class MasterDataBaseController {

    @Resource
    private MasterDataBaseService masterDataBaseService;

    @Resource
    private MasterTableService masterTableService;

    private String masterDataBase;


    @RequestMapping(value = "/findAllTableName",method = RequestMethod.POST)
    public ServiceResponse<List<DataBaseEntity>> findAllTableName(){
        masterDataBase = DataBaseConfig.getMasterDataBase();
        List<DataBaseEntity> list = null;
        DataBaseEntity entity = new DataBaseEntity();
        entity.setDataBaseName(masterDataBase);
        list = masterDataBaseService.findAllTableName(entity);
        return ServiceResponse.ok(list);
    }

    @RequestMapping(value = "/findTableStructure",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ServiceResponse<List<TableEntity>> findTableStructure(@RequestBody JSONObject jsonParam){
        masterDataBase = DataBaseConfig.getMasterDataBase();
        List<TableEntity> list = null;
        //String tableName = "\"" + jsonParam.getString("tableName") + "\"";
        String tableName = jsonParam.getString("tableName");
        DataBaseEntity entity = new DataBaseEntity(masterDataBase,tableName);
        list = masterTableService.findTableStructure(entity);
        return ServiceResponse.ok(list);
    }

}
