package com.mingcloud.data.service.impl.slave;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.mapper.slave.SlaveTableMapper;
import com.mingcloud.data.service.slave.SlaveTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("SlaveTableService")
@DS("slave")
public class SlaveTableServiceImpl extends ServiceImpl<SlaveTableMapper, TableEntity> implements SlaveTableService {
    private static final Logger logger= LoggerFactory.getLogger(SlaveTableServiceImpl.class);

    @Resource
    private SlaveTableMapper slaveTableMapper;

    @Override
    @DS("slave")
    public List<TableEntity> findTableStructure(Object obj) {
        List<TableEntity> list = null;
        try {
            list =  slaveTableMapper.selectTableStructure(obj);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }

}
