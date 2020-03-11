package com.mingcloud.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * User实体类
 * </p>
 *
 * @package: com.mingcloud.data.entity.User
 * @description: User实体类
 * @author: simin.x
 * @Data / @NoArgsConstructor / @AllArgsConstructor / @Builder 都是 lombok 注解
 * @TableName("multi_user") 是 Mybatis-Plus 注解，主要是当实体类名字和表名不满足 驼峰和下划线互转 的格式时
 * ，用于表示数据库表名
 * @TableId(type = IdType.ID_WORKER) 是 Mybatis-Plus 注解，
 * 主要是指定主键类型，这里我使用的是 Mybatis-Plus 基于 twitter 提供的 雪花算法
 */

@Data
@TableName("t_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -1923859222295750467L;
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER)
    private int id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;
}