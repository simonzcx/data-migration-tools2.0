package com.mingcloud.data.mapper.slave;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingcloud.data.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface SlaveTableMapper extends BaseMapper<TableEntity> {
    /**
     * 查找数据库表所有列名称
     */
    List<TableEntity> selectTableStructure(Object obj);
}
