package com.udun_demo.service;

import com.baomidou.mybatisplus.service.IService;
import com.udun_demo.dao.entity.User;
import com.udun_demo.support.common.CommonException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
public interface IUserService extends IService<User> {

    /**
     * 注册用户
     * @param username
     * @param password
     */
    User register(String username, String password);

    /**
     * 校验用户名，如果存在抛出异常
     * @param username
     * @return
     */
    void checkUserName(String username);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User queryUser(String username);

    /**
     * 登录，校验密码和用户名是否正确
     * @param username
     * @param password
     */
    User login(String username, String password);

    void checkTransactionPassword(String username, String transactionPassword) throws CommonException;

    void updateTransactionPassword(String username, String transactionPassword, String newTransactionPassword);

    void changePassword(String username, String password, String newPassword);
}
