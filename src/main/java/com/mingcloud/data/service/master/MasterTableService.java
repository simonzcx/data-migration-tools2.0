package com.mingcloud.data.service.master;



import com.baomidou.mybatisplus.extension.service.IService;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.TableEntity;

import java.util.List;

public interface MasterTableService extends IService<TableEntity> {
    /**
     * 查找数据库表所有列名称
     */
    List<TableEntity> findTableStructure(DataBaseEntity entity);
}
