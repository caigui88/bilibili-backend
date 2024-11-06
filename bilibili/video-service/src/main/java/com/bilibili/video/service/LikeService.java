package com.bilibili.video.service;


import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.LikeDTO;

public interface LikeService {
    Result<Boolean> like(LikeDTO likeDTO);
    Result<Boolean> recallLike(LikeDTO likeDTO);
}
