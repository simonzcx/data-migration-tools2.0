package com.mingcloud.data.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.SysSqlAnalysisEntity;
import com.mingcloud.data.mapper.SysSqlAnalysisMapper;
import com.mingcloud.data.service.SysSqlAnalysisService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@DS("master")
public class SysSqlAnalysisServiceImpl implements SysSqlAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(SysSqlAnalysisServiceImpl.class);
    @Resource
    private SysSqlAnalysisMapper sysSqlAnalysisMapper;

    private String filePath = DataBaseConfig.getFilePath();

    private String fileName = DataBaseConfig.getFileName();

    private String recordSqlTable;

    private String masterDataBase;

    private String slaveDataBase;

    @Override
    @DS("master")
    public Boolean sysSqlAnalysisInit(DataBaseEntity entity) {
        int existTable = sysSqlAnalysisMapper.existTable(entity);
        if (existTable == 0) {
            sysSqlAnalysisMapper.createTable(entity);
        } else if (existTable > 0) {
            int dropTable = sysSqlAnalysisMapper.dropTable(entity);
            if (dropTable == 0) {
                sysSqlAnalysisMapper.createTable(entity);
            } else {
                logger.info("删除表：{} 失败！", entity.getTableName());
            }
        } else {
            return false;
        }
        logger.info("初始化成功，新建表：{}", entity.getTableName());
        return true;
    }

    @Override
    @DS("master")
    public void generateSqlFile(DataBaseEntity entity) {
        List<SysSqlAnalysisEntity> entityList = sysSqlAnalysisMapper.selectSqls(entity);
        fileName = DataBaseConfig.getFileName();
        filePath = DataBaseConfig.getFilePath();
        List<String> sqlList = new ArrayList<>();
        for (SysSqlAnalysisEntity en : entityList) {
            sqlList.add(en.getSqls());
        }
        try {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                this.deleteIfExists(file);
                file.createNewFile();
            }
            /*
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sqlList.get(i)+";");
            writer.newLine();
            writer.close();
            */
            String sql = null;
            for (int i = 0; i < sqlList.size(); i++) {
                sql = sqlList.get(i) + ";";
                FileUtils.writeStringToFile(file, sql + "\r\n", "UTF-8", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @DS("master")
    public ServiceResponse<String> downloadSqlFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataBaseEntity entity = new DataBaseEntity();
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        entity.setTableName(recordSqlTable);

        this.generateSqlFile(entity);
        File file = new File(filePath + fileName);
        response.setCharacterEncoding("utf-8");

        OutputStream outputStream = null;
        try {
            //统一转换编码
            fileName = URLEncoder.encode(fileName, "utf-8");
            InputStream inputStream = new FileInputStream(file);
            // 设置输出的格式
            response.setContentType("application/x-msdownload:charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            outputStream = response.getOutputStream();
            //输入流、输出流的转换 commons-io-xx.jar的方法
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return ServiceResponse.ok("下载成功！");
    }

    @Override
    @DS("master")
    public void findDataInsertInto(TableDto dto) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataBaseMap = new HashMap<>();
        Map<String, Object> tableMap = new HashMap<>();
        masterDataBase = DataBaseConfig.getMasterDataBase();
        slaveDataBase = DataBaseConfig.getSlaveDataBase();
        String newDataBase = "`" + masterDataBase + "`";
        String oldDataBase = "`" + slaveDataBase + "`";
        dataBaseMap.put("newDataBase", newDataBase);
        dataBaseMap.put("oldDataBase", oldDataBase);
        /* ------------------------------------------------------- */
        tableMap.put("newTable", dto.getNewTable());
        tableMap.put("oldTable", dto.getOldTable());
        /* ------------------------------------------------------- */
        map.put("dataBaseMap", dataBaseMap);
        map.put("tableMap", tableMap);
        map.put("columnMap", dto.columnMap);

        sysSqlAnalysisMapper.selectDataInto(map);

    }

    @Override
    public int dropTable(DataBaseEntity entity) throws DataAccessException {
        return sysSqlAnalysisMapper.dropTable(entity);
    }

    /**
     * 删除文件或文件夹
     */
    public void deleteIfExists(File file) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File temp : files) {
                        deleteIfExists(temp);
                    }
                }
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            }
        }
    }

}
