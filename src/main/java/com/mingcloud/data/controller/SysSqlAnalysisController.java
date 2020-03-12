package com.mingcloud.data.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.service.SysSqlAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;


@RestController
@RequestMapping(value = "/SysSqlAnalysis")
public class SysSqlAnalysisController {
    private static final Logger logger = LoggerFactory.getLogger(SysSqlAnalysisController.class);

    @Autowired
    private SysSqlAnalysisService sysSqlAnalysisService;

    private String recordSqlTable;

    private String masterDataBase;

    public SysSqlAnalysisController() {
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        masterDataBase = DataBaseConfig.getMasterDataBase();
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public ServiceResponse<String> SysSqlAnalysisInit() throws IOException {
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        masterDataBase = DataBaseConfig.getMasterDataBase();

        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(recordSqlTable);
        //entity.setTableName("`"+recordSqlTable+"`");
        entity.setDataBaseName(masterDataBase);
        Boolean init = null;
        init = sysSqlAnalysisService.sysSqlAnalysisInit(entity);
        if (init) {
            return ServiceResponse.ok("创建sql记录表:" + entity.getTableName() + " 成功，初始化完成！");
        } else {
            return ServiceResponse.error("初始化失败！");
        }
    }

    @RequestMapping(value = "/downloadSqlFile", method = RequestMethod.POST)
    public ServiceResponse<String> downloadSqlFile(HttpServletRequest request, HttpServletResponse response) {
        ServiceResponse result = null;
        try {
            result = sysSqlAnalysisService.downloadSqlFile(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/dataMigration", method = RequestMethod.POST)
    public ServiceResponse dataMigration(@RequestBody TableDto dto) {
        //TableDto dto = (TableDto)JSONObject.toJavaObject(param,TableDto.class);
        //TableDto dto = new TableDto();
        //String newTable = param.getString("newTable");
        //String oldTable = param.getString("oldTable");
        /*dto.setNewTable(newTable);
        dto.setOldTable(oldTable);*/
        String newTable = dto.getNewTable();
        String oldTable = dto.getOldTable();
        try {
            sysSqlAnalysisService.findDataInsertInto(dto);
            return ServiceResponse.ok("数据：" + oldTable + "迁移到：" + newTable + "成功！");
        } catch (DataAccessException e) {
            logger.error("主键值重复：" + e.getMessage());
            return ServiceResponse.error("主键值重复!");
        }

    }
    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public ServiceResponse dropRecordSqlTable(){
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(recordSqlTable);
        try {
            sysSqlAnalysisService.dropTable(entity);
            logger.info("删除sql记录表：{} 成功！",recordSqlTable);
            return ServiceResponse.ok("删除sql记录表：" + recordSqlTable + "成功！");
        } catch (DataAccessException e){
            logger.info("删除sql记录表：{} 失败！异常：{}",recordSqlTable,e.getMessage());
            if(e.getMessage().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException")){
                return ServiceResponse.error("sql记录表：" + recordSqlTable + "不存在，删除失败！");
            }
            return ServiceResponse.error("删除sql记录表：" + recordSqlTable + "失败！" + e.getRootCause());
        }
    }
}
