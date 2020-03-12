package com.mingcloud.data.service.master;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.TableEntity;

import java.util.List;

public interface MasterTableService extends IService<TableEntity> {
    /**
     * @return List<TableEntity>
     * @throws
     * @Title: findTableStructure
     * @Description: 获取表所有字段
     * @author simon
     * @date 2020/03/03
     */
    List<TableEntity> findTableStructure(DataBaseEntity entity);
}
