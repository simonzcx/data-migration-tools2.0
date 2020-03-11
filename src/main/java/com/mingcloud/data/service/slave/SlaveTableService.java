package com.mingcloud.data.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingcloud.data.entity.TableEntity;

import java.util.List;

public interface SlaveTableService extends IService<TableEntity> {
    /**
     * 查找数据库表所有列名称
     */
    List<TableEntity> findTableStructure(Object obj);
}
