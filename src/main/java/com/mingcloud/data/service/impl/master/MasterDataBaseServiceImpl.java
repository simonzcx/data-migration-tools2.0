package com.mingcloud.data.service.impl.master;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.mapper.master.MasterDataBaseMapper;
import com.mingcloud.data.service.master.MasterDataBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service("MasterDataBaseService")
@DS("master")
public class MasterDataBaseServiceImpl
        extends ServiceImpl<MasterDataBaseMapper, DataBaseEntity> implements MasterDataBaseService {
    private static final Logger logger= LoggerFactory.getLogger(MasterDataBaseServiceImpl.class);

    @Resource
    private MasterDataBaseMapper masterDataBaseMapper;

    @Override
    @DS("master")
    public List<DataBaseEntity> findAllTableName(DataBaseEntity entity) {
        List<DataBaseEntity> list = null;
        try {
            list = masterDataBaseMapper.selectAllTableName(entity);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }
}
