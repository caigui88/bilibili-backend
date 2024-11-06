package com.bilibili.user.service;


import com.bilibili.user.domain.dto.FollowDTO;
import com.bilibili.user.domain.vo.IdolOrFansVO;
import com.bilibili.common.util.Result;

import java.util.List;

public interface FollowService {
    Result<Boolean> follow(FollowDTO follow);
    Result<Boolean> recallFollow( FollowDTO follow);
    Result<List<IdolOrFansVO>> getIdolOrFansListVO(int userId, int type);
}
