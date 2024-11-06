package com.bilibili.video.service;


import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.AddDanmakuDTO;
import com.bilibili.video.domain.vo.DanmakuVO;

import java.util.List;

public interface DanmakuService{
    Result<Boolean> addDanmaku(AddDanmakuDTO addDanmakuDTO);
    Result<List<DanmakuVO>> getDanmaku(Integer videoId);
}
