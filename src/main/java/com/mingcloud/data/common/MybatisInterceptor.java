package com.mingcloud.data.common;

import com.alibaba.fastjson.JSON;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.SysSqlAnalysisEntity;
import com.mingcloud.data.mapper.SysSqlAnalysisMapper;
import com.mingcloud.data.service.SysSqlAnalysisService;
import com.mingcloud.data.service.master.MasterDataBaseService;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/*
 * @Intercepts
 * 插件签名，告诉mybatis插件拦截哪个对象的哪个方法
 * */
//
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisInterceptor.class);

    @Resource
    private SysSqlAnalysisMapper sysSqlAnalysisMapper;

    @Resource
    private SysSqlAnalysisService sysSqlAnalysisService;

    @Autowired
    private DataBaseConfig dataBaseConfig;

    @Override
    /*
     * Object intercept(Invocation invocation)是实现拦截逻辑的地方，拦截目标对象的目标方法，
     * 内部要通过invocation.proceed()显式地推进责任链前进，也就是调用下一个拦截器拦截目标方法。
     * */
    public Object intercept(Invocation invocation) throws Throwable {
        SysSqlAnalysisEntity analysis = new SysSqlAnalysisEntity();
        // 获取xml中的一个select/update/insert/delete节点，是一条SQL语句
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //获取当前SQL命令类型
        SqlCommandType commandType = mappedStatement.getSqlCommandType();
        Object parameter = null;
        // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
            //analysis.setParameter(JSON.toJSONString(parameter));
        }
        // 获取拦截器指定的方法类型

        // 获取到节点的id,即sql语句的id
        String sqlId = mappedStatement.getId();

        //analysis.setMapper(sqlId);

        // BoundSql就是封装myBatis最终产生的sql类
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // 获取节点的配置
        Configuration configuration = mappedStatement.getConfiguration();

        Object returnVal = invocation.proceed();

        //获取sql语句
        String sql = getSql(configuration, boundSql);

        //analysis.setSqls(sql);
        /*String methodName = invocation.getMethod().getName();
        log.info("NormalPlugin, methodName; {}, commandType: {}", methodName, commandType);
        if(methodName.equals("update")){
            if(commandType.equals(SqlCommandType.INSERT)){
                analysis.setParameter(JSON.toJSONString(parameter));
                analysis.setMapper(sqlId);
                analysis.setSqls(sql);
                sysSqlAnalysisMapper.insertRecordSql(analysis);
            }
        }*/

        if (sqlId.equals("com.mingcloud.data.mapper.SysSqlAnalysisMapper.selectDataInto")) {
            analysis.setParameter(JSON.toJSONString(parameter));
            analysis.setMapper(sqlId);
            analysis.setSqls(sql);
            DataBaseEntity entity = new DataBaseEntity();
            entity.setTableName(dataBaseConfig.getRecordSqlTable());
            //String databaseName = masterDataBaseService.selectDatabaseNameByMySQL();
            String databaseName = dataBaseConfig.getMasterDataBaseName();
            int existTable = sysSqlAnalysisMapper.existTable(entity);
            if (existTable > 0) {
                sysSqlAnalysisMapper.insertRecordSql(analysis);
            } else {
                entity.setDataBaseName(databaseName);
                Boolean init = sysSqlAnalysisService.sysSqlAnalysisInit(entity);
                if (init) {
                    logger.info("自动初始化，创建sql记录表:{}成功，初始化完成！", entity.getTableName());
                    sysSqlAnalysisMapper.insertRecordSql(analysis);
                } else {
                    logger.info("自动初始化失败！");
                }
            }
        }
        logger.info("Mybatis 拦截器获取SQL:{}", sql);
        // 执行完上面的任务后，不改变原有的sql执行过程
        return returnVal;
    }

    @Override
    /*
     * 包装目标对象
     * Object plugin(Object target) 就是用当前这个拦截器生成对目标target的代理，
     * 实际是通过Plugin.wrap(target,this) 来完成的，把目标target和拦截器this传给了包装函数
     * */
    public Object plugin(Object target) {
        //为当前对象创建单例对象并返回
        return Plugin.wrap(target, this);
    }

    @Override
    /*
     *  setProperties(Properties properties)用于设置额外的参数，参数配置在拦截器的Properties节点里
     * */
    public void setProperties(Properties arg0) {
    }

    /**
     * 获取SQL
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    private String getSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterObject == null || parameterMappings.size() == 0) {
            return sql;
        }
        // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
        } else {
            /*
             MetaObject主要是封装了originalObject对象，
             提供了get和set的方法用于获取和设置originalObject的属性值,
             主要支持对JavaBean、Collection、Map三种类型对象的操作
             */
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(obj));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    // 该分支是动态sql
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(obj));
                }
            }
        }
        return sql;
    }

    /*
     如果参数是String，则添加单引号；
     如果是日期，则转换为时间格式器并加单引号；
     对参数是null和不是null的情况作了处理
     */
    private String getParameterValue(Object obj) {
        String value = null;
        //判断obj是否为String的实例
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }
}

