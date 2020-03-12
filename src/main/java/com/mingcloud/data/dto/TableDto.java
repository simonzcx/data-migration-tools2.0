package com.mingcloud.data.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class TableDto {

    //private String newDataBase;

    //private String oldDataBase;

    private String newTable;

    private String oldTable;

    //public Map<String,Object> columnMap = new LinkedHashMap<>();
    public Map<String,Object> columnMap;
}
