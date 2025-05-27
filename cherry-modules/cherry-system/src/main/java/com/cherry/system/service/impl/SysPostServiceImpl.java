package com.cherry.system.service.impl;

import com.cherry.system.domain.vo.SysPostVo;
import com.cherry.system.mapper.SysPostMapper;
import com.cherry.system.service.ISysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位信息 服务层处理
 * @author keer
 * @date 2025-05-27
 */
@RequiredArgsConstructor
@Service
public class SysPostServiceImpl implements ISysPostService {
    // todo
    private final SysPostMapper baseMapper;
    @Override
    public List<SysPostVo> selectPostsByUserId(Long userId) {
        return baseMapper.selectPostsByUserId(userId);
    }
}
