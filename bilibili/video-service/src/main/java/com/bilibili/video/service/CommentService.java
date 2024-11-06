package com.bilibili.video.service;


import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.CommentDTO;
import com.bilibili.video.domain.vo.VideoCommentVO;

public interface CommentService {
    Result<Boolean> commentToComment(CommentDTO commentDTO);
    Result<VideoCommentVO> getComment(Integer videoId, Integer userId, Integer type);
}
