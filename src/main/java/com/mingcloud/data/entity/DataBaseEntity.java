package com.mingcloud.data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataBaseEntity {

    private String dataBaseName;

    private String tableName;

    public DataBaseEntity(){}

    public DataBaseEntity(String dataBaseName, String tableName){
        this.dataBaseName = dataBaseName;
        this.tableName = tableName;
    }

}
