package com.bilibili.user.service;


import com.bilibili.user.domain.vo.UserInfoVO;
import com.bilibili.common.util.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {
    Result<UserInfoVO> getUserInfo(Integer selfId, Integer visitedId);

    Result<Boolean> editSelfInfo(MultipartFile file, Integer userId, String nickname, String intro) throws Exception;
}
