package com.mingcloud.data.mapper.master;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MasterTableMapper extends BaseMapper<TableEntity> {
    /**
     * 查找数据库表所有列名称
     */
    List<TableEntity> selectTableStructure(DataBaseEntity entity);


}
