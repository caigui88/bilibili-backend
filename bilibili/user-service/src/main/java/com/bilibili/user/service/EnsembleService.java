package com.bilibili.user.service;


import com.bilibili.user.domain.dto.AddOrUpdateEnsembleDTO;
import com.bilibili.user.domain.dto.AddToEnsembleDTO;
import com.bilibili.user.domain.dto.DeleteFromEnsembleDTO;
import com.bilibili.common.domain.entity.user.VideoEnsemble;
import com.bilibili.user.domain.vo.AllVideoInEnsembleVO;
import com.bilibili.user.domain.vo.EnsembleVideoVO;
import com.bilibili.common.util.Result;

import java.util.List;

public interface EnsembleService {
    Result<List<VideoEnsemble>> getEnsembleList(Integer userId);
    Result<Boolean> addOrUpdateEnsemble(AddOrUpdateEnsembleDTO addOrUpdateEnsembleDTO);
    Result<List<EnsembleVideoVO>> getEnsembleVideo(int userId);
    Result<AllVideoInEnsembleVO> getAllVideoInEnsemble(Integer userId, Integer videoId);
    Result<Boolean> addToEnsemble(AddToEnsembleDTO addToEnsembleDTO);
    Result<Boolean> deleteFromEnsemble(DeleteFromEnsembleDTO deleteFromEnsembleDTO);
}
