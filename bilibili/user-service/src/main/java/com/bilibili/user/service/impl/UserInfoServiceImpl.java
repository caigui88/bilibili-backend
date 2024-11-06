package com.bilibili.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bilibili.user.domain.vo.UserInfoVO;
import com.bilibili.user.service.UserInfoService;
import com.bilibili.api.client.SendNoticeClient;
import com.bilibili.common.util.Result;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.user.Follow;
import com.bilibili.common.mapper.user.UserMapper;
import com.bilibili.common.mapper.user.FollowMapper;
import com.bilibili.user.mapper.UserCenterServiceMapper;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bilibili.user.constant.Constant.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    @Lazy
    MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName ;

    @Autowired
    UserCenterServiceMapper userCenterServiceMapper;
    String filePath = "https://caiguibilibili.com/";

    @Resource
    SendNoticeClient sendNoticeClient;


    @Override
    public Result<UserInfoVO> getUserInfo(Integer selfId, Integer visitedId) {
        MPJLambdaWrapper<User> fansCountWrapper = new MPJLambdaWrapper<>();
        MPJLambdaWrapper<User> idolCountWrapper = new MPJLambdaWrapper<>();

        fansCountWrapper.eq(User::getId, visitedId);
        fansCountWrapper.leftJoin(Follow.class,Follow::getIdolId, User::getId);
        idolCountWrapper.eq(User::getId, visitedId);
        idolCountWrapper.leftJoin(Follow.class,Follow::getFansId, User::getId);

        UserInfoVO userInfoVO = userCenterServiceMapper.getUserInfo(visitedId)
                .setFansCount(Math.toIntExact(userMapper.selectJoinCount(fansCountWrapper)))
                .setIdolCount(Math.toIntExact(userMapper.selectJoinCount(idolCountWrapper)));

        if(selfId > 0){
            LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
            followLambdaQueryWrapper.eq(Follow::getFansId, selfId);
            followLambdaQueryWrapper.eq(Follow::getIdolId, visitedId);
            if(followMapper.selectCount(followLambdaQueryWrapper) > 0){
                userInfoVO.setIsFollowing(true);
            }else {
                userInfoVO.setIsFollowing(false);
            }
        }
        return Result.success(userInfoVO);
    }

    /**
     * 修改用户信息并发送数据同步消息
     * @param file
     * @param userId
     * @param nickname
     * @param intro
     * @return
     * @throws Exception
     */
    @Override
    public Result<Boolean> editSelfInfo(
            MultipartFile file,
            Integer userId,
            String nickname,
            String intro) throws Exception {
        Map<String,Object> map = new HashMap<>();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId);
        map.put(TABLE_ID, userId);
        map.put(OPERATION_TYPE, OPERATION_TYPE_UPDATE);
        map.put(TABLE_NAME, USER_TABLE_NAME);
        if(file != null){
            String coverName  = UUID.randomUUID().toString().substring(0,10) + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), -1, 10485760)
                            .bucket(bucketName).object(coverName).build());
            String url = filePath + coverName + "/" + coverName;
            map.put(USER_COVER, url);
            wrapper.set(User::getCover, url);
        }
        if(nickname != null){
            map.put(USER_NICKNAME, nickname);
            wrapper.set(User::getNickname, nickname);
        }
        if(intro != null){
            map.put(USER_INTRO, intro);
            wrapper.set(User::getIntro, intro);
        }
        userMapper.update(null, wrapper);
        sendNoticeClient.sendDBChangeNotice(map);
        return Result.success(true);
    }
}
