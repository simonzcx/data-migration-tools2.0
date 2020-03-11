package com.mingcloud.data.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.SysSqlAnalysisEntity;
import com.mingcloud.data.mapper.SysSqlAnalysisMapper;
import com.mingcloud.data.service.SysSqlAnalysisService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@DS("master")
public class SysSqlAnalysisServiceImpl implements SysSqlAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(SysSqlAnalysisServiceImpl.class);
    @Resource
    private SysSqlAnalysisMapper sysSqlAnalysisMapper;

    @Autowired
    private DataBaseConfig dataBaseConfig;


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
                log.info("删除表：{} 失败！", entity.getTableName());
            }
        } else {
            return false;
        }
        log.info("初始化成功，新建表：{}", entity.getTableName());
        return true;
    }

    @Override
    @DS("master")
    public void generateSqlFile(DataBaseEntity entity) {
        List<SysSqlAnalysisEntity> entityList = sysSqlAnalysisMapper.selectSqls(entity);
        String filePath = dataBaseConfig.getFilePath();
        String fileName = dataBaseConfig.getFileName();

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
        entity.setTableName(new String("sys_sql_analysis"));
        this.generateSqlFile(entity);
        String filePath = dataBaseConfig.getFilePath();
        String fileName = dataBaseConfig.getFileName();
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
