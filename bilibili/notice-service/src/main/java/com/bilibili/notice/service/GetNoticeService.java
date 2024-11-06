package com.bilibili.notice.service;


import com.bilibili.common.util.Result;
import com.bilibili.notice.domain.vo.CommentNoticeVO;
import com.bilibili.notice.domain.vo.DynamicVideoVO;
import com.bilibili.notice.domain.vo.LikeNoticeVO;
import com.bilibili.notice.domain.vo.UnReadNoticeCountVO;

import java.util.List;

public interface GetNoticeService {
    Result<UnReadNoticeCountVO> getNoticeCount(Integer userId);
    Result<List<LikeNoticeVO>> getLikeNotice(Integer userId);
    Result<List<CommentNoticeVO>> getCommentNotice(Integer userId);
    Result<List<DynamicVideoVO>> getDynamicVideo(Integer userId);
}
