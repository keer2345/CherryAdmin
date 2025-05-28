package com.cherry.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.cherry.system.domain.vo.SysUserVo;
import com.cherry.system.mapper.SysRoleMapper;
import com.cherry.system.mapper.SysUserMapper;
import com.cherry.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户 业务层处理
 * @author keer
 * @date 2025-05-28
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl implements ISysUserService {
    // todo
    private final SysUserMapper baseMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public SysUserVo selectUserById(Long userId) {
        SysUserVo user = baseMapper.selectVoById(userId);
        if(ObjUtil.isNull(user)){
            return user;
        }
        user.setRoles(roleMapper.selectRolesByUserId(user.getUserId()));
        return user;
    }
}
