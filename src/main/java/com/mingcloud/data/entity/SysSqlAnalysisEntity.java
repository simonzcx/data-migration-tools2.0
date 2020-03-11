package com.mingcloud.data.entity;

public class SysSqlAnalysisEntity {
    // 主键
    private String id;
    // 请求参数
    private String parameter;
    // mybatis mapper 命名空间
    private String mapper;
    // 完整sql 语句
    private String sqls;
    // sql 性能分析
    private String analysis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper == null ? null : mapper.trim();
    }


    public String getSqls() {
        return sqls;
    }

    public void setSqls(String sqls) {
        this.sqls = sqls;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis == null ? null : analysis.trim();
    }

}
