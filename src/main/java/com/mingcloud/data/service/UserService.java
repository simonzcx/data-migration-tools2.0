package com.mingcloud.data.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mingcloud.data.entity.User;

/**
 * <p>
 * 数据服务层
 * </p>
 *
 * @package: com.mingcloud.data.service
 * @description: 数据服务层
 * @author: simon.x
 */
public interface UserService extends IService<User> {

    /**
     * 添加 User
     *
     * @param user 用户
     */
    void addUser(User user);
}
