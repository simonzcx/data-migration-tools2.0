package com.mingcloud.data.controller;

import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.service.SysSqlAnalysisService;
import com.mingcloud.data.service.master.MasterDataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/SysSqlAnalysis")
public class SysSqlAnalysisController {

    @Autowired
    private SysSqlAnalysisService sysSqlAnalysisService;

    @Autowired
    private MasterDataBaseService masterDataBaseService;

    @Autowired
    private DataBaseConfig dataBaseConfig;

    @RequestMapping(value = "/init" , method = RequestMethod.POST)
    public ServiceResponse<String> SysSqlAnalysisInit() throws IOException {
        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(dataBaseConfig.getRecordSqlTable());
        String databaseName = masterDataBaseService.selectDatabaseNameByMySQL();
        entity.setDataBaseName(databaseName);
        Boolean init = sysSqlAnalysisService.sysSqlAnalysisInit(entity);
        if(init){
            return ServiceResponse.ok("创建sql记录表:"+ entity.getTableName() +" 成功，初始化完成！");
        } else {
            return ServiceResponse.error("初始化失败！");
        }
    }
    @RequestMapping(value = "/downloadSqls" , method = RequestMethod.POST)
    public ServiceResponse<String> downloadSqls(HttpServletRequest request, HttpServletResponse response){
        ServiceResponse result = null;
        try {
            result = sysSqlAnalysisService.downloadSqlFile(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
