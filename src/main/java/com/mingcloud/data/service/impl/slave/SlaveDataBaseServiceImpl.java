package com.mingcloud.data.service.impl.slave;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.mapper.slave.SlaveDataBaseMapper;
import com.mingcloud.data.service.slave.SlaveDataBaseService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service("SlaveDataBaseService")
@DS("slave")
public class SlaveDataBaseServiceImpl
        extends ServiceImpl<SlaveDataBaseMapper, DataBaseEntity> implements SlaveDataBaseService {
    private static final Logger logger= LoggerFactory.getLogger(SlaveDataBaseServiceImpl.class);

    @Resource
    private SlaveDataBaseMapper slaveDataBaseMapper;

    @Override
    @DS("slave")
    public List<DataBaseEntity> findAllTableName(DataBaseEntity entity) {
        List<DataBaseEntity> list = null;
        try {
            list = slaveDataBaseMapper.selectAllTableName(entity);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }
}
