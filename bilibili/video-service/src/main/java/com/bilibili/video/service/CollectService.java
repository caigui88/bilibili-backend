package com.bilibili.video.service;


import com.bilibili.common.domain.entity.video.audience_reactions.CollectGroup;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.CollectGroupDTO;
import com.bilibili.video.domain.dto.CollectDTO;
import com.bilibili.video.domain.vo.CollectGroupVO;
import com.bilibili.video.domain.vo.CollectVideoVO;

import java.util.List;

public interface CollectService {
    Result<Boolean> collect(List<CollectDTO> collectDTO);
    Result<Boolean> editCollectGroup(CollectGroupDTO createCollectGroupDTO);
    Result<Boolean> deleteCollectGroup(CollectGroupDTO createCollectGroupDTO);
    Result<List<CollectGroup>> getCollectGroup(Integer userId);
    Result<List<CollectVideoVO>> getCollectVideo(Integer userId);
    Result<List<CollectGroupVO>> getVideoToCollectGroup(Integer userId, Integer videoId);
}
