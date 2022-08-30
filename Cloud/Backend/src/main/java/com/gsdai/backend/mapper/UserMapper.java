package com.gsdai.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsdai.backend.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
