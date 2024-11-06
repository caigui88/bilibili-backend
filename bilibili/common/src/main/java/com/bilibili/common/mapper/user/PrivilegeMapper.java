package com.bilibili.common.mapper.user;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bilibili.common.domain.entity.user.Privilege;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface PrivilegeMapper extends BaseMapper<Privilege> {
}
