package com.bilibili.video.service;


import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.DeleteHistoryVideoDTO;
import com.bilibili.video.domain.vo.CommendVideoVO;
import com.bilibili.video.domain.vo.DetailVideoVO;
import com.bilibili.video.domain.vo.FirstPageVideoVO;
import com.bilibili.video.domain.vo.HistoryVideoVO;

import java.util.List;

public interface PlayService {
    Result<Boolean> addPlayRecord(int videoId, int userId);
    Result<Boolean> deleteHistoryVideo(DeleteHistoryVideoDTO deleteHistoryVideoDTO);
    Result<DetailVideoVO> getDetailVideo(Integer videoId, Integer userId, String collectGroupId);
    Result<List<CommendVideoVO>> getRecommendVideo(String videoId);
    Result<List<FirstPageVideoVO>> getFirstPageVideoResponse(Integer count);
    Result<List<HistoryVideoVO>> getHistoryVideo(int userId);
}
