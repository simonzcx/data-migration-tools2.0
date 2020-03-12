package com.mingcloud.data.service;


import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;
import org.springframework.dao.DataAccessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SysSqlAnalysisService {
    /**
     * @return Boolean
     * @throws
     * @Title: sysSqlAnalysisInit
     * @Description: 初始化 数据库创建表sys_sql_analysis
     * @author simon
     * @date 2020/03/09
     */
    Boolean sysSqlAnalysisInit(DataBaseEntity entity);
    /**
     * @return void
     * @throws
     * @Title: generateSqlFile
     * @Description: 生成sql文件
     * @author simon
     * @date 2020/03/10
     */
    void generateSqlFile(DataBaseEntity entity);
    /**
     * @return void
     * @throws
     * @Title: downloadSqlFile
     * @Description: 下载sql文件
     * @author simon
     * @date 2020/03/10
     */
    ServiceResponse<String> downloadSqlFile(HttpServletRequest request, HttpServletResponse response) throws IOException;
    /**
     * @return void
     * @throws
     * @Title: downloadSqlFile
     * @Description: 数据迁移
     * @author simon
     * @date 2020/03/11
     */
    void findDataInsertInto(TableDto dto) throws DataAccessException;
    /**
     * @return int
     * @throws
     * @Title: dropTable
     * @Description: 删除表
     * @author simon
     * @date 2020/03/12
     */
    int dropTable(DataBaseEntity entity) throws DataAccessException;
}
