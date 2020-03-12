package com.mingcloud.data.service.master;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mingcloud.data.entity.DataBaseEntity;

import java.io.IOException;
import java.util.List;

public interface MasterDataBaseService extends IService<DataBaseEntity> {
    /**
     * @return String
     * @throws
     * @Title: findAllTableName
     * @Description: 获取所有表名
     * @author simon
     * @date 2020/03/06
     */
    List<DataBaseEntity> findAllTableName(DataBaseEntity entity);

    /**
     * @return String
     * @throws IOException
     * @Title: selectDatabaseNameByMySQL
     * @Description: 获取mysql连接时的数据库名
     * @author simon
     * @date 2020/03/09
     */
    /*String selectDatabaseNameByMySQL() throws IOException;*/

}
