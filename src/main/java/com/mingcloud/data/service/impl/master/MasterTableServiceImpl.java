package com.mingcloud.data.service.impl.master;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.mapper.master.MasterTableMapper;
import com.mingcloud.data.service.master.MasterTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("MasterTableService")
@DS("master")
public class MasterTableServiceImpl extends ServiceImpl<MasterTableMapper, TableEntity> implements MasterTableService {
    private static final Logger logger= LoggerFactory.getLogger(MasterTableServiceImpl.class);

    @Resource
    private MasterTableMapper masterTableMapper;

    @Override
    @DS("master")
    public List<TableEntity> findTableStructure(DataBaseEntity entity) {
        List<TableEntity> list = null;
        try {
            list =  masterTableMapper.selectTableStructure(entity);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }
}
