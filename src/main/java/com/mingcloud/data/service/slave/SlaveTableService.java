package com.mingcloud.data.service.slave;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mingcloud.data.entity.TableEntity;

import java.util.List;

public interface SlaveTableService extends IService<TableEntity> {
    /**
     * @return List<TableEntity>
     * @throws
     * @Title: findTableStructure
     * @Description: 获取表所有字段
     * @author simon
     * @date 2020/03/03
     */
    List<TableEntity> findTableStructure(Object obj);
}
