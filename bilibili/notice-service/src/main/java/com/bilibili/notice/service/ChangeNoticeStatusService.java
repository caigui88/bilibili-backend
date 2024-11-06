package com.bilibili.notice.service;


import com.bilibili.common.util.Result;
import com.bilibili.notice.domain.dto.CommentNoticeStatusChangeDTO;
import com.bilibili.notice.domain.dto.DynamicVideoStatusChangeDTO;
import com.bilibili.notice.domain.dto.LikeNoticeStatusChangeDTO;

public interface ChangeNoticeStatusService {
    Result<Boolean> changeLikeNoticeStatus(LikeNoticeStatusChangeDTO likeNoticeStatusChangeDTO);
    Result<Boolean> changeCommentNoticeStatus(CommentNoticeStatusChangeDTO commentNoticeStatusChangeDTO);
    Result<Boolean> changeDynamicVideoStatus(DynamicVideoStatusChangeDTO videoStatusChangeRequest);
}
