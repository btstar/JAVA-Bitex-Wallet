package com.udun_demo.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.udun_demo.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2019-06-27
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}
