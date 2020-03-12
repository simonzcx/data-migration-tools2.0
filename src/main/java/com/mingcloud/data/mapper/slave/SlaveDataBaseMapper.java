package com.mingcloud.data.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mingcloud.data.entity.DataBaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SlaveDataBaseMapper extends BaseMapper<DataBaseEntity> {

    List<DataBaseEntity> selectAllTableName(DataBaseEntity entity);

}
