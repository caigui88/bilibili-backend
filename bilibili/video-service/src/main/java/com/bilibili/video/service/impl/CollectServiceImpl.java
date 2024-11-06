package com.bilibili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.api.client.SendNoticeClient;
import com.bilibili.common.domain.entity.video.audience_reactions.Collect;
import com.bilibili.common.domain.entity.video.audience_reactions.CollectGroup;
import com.bilibili.common.mapper.video.audience_reactions.CollectGroupMapper;
import com.bilibili.common.mapper.video.audience_reactions.CollectMapper;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.CollectGroupDTO;
import com.bilibili.video.domain.dto.CollectDTO;
import com.bilibili.video.domain.vo.CollectGroupVO;
import com.bilibili.video.domain.vo.CollectVideoVO;
import com.bilibili.video.mapper.VideoServiceMapper;
import com.bilibili.video.service.CollectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 收藏
 */
@Service
public class CollectServiceImpl implements CollectService {
    @Resource
    CollectMapper collectMapper;
    @Resource
    CollectGroupMapper collectGroupMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    VideoServiceMapper videoServiceMapper;
    /**
     *收藏视频
     */
    @Override
    public Result<Boolean> collect(List<CollectDTO> collects) {
        for(CollectDTO collectDTO : collects){
            //如果收藏夹的type值是true则为收藏否则是取消收藏
            if(collectDTO.getType()){
                Collect collect=new Collect();
                collect.setCollectGroupId(collectDTO.getCollectGroupId());
                collect.setVideoId(collectDTO.getVideoId());
                collectMapper.insert(collect);
            }else {
                LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Collect::getVideoId, collectDTO.getVideoId());
                wrapper.eq(Collect::getCollectGroupId, collectDTO.getCollectGroupId());
                collectMapper.delete(wrapper);
            }
        }
        return Result.success(true);
    }
    /**
     *获取视频详情页中收藏夹列表（标注了视频相对于收藏夹的状态是否已收藏）
     */
    @Override
    public Result<List<CollectGroupVO>> getVideoToCollectGroup(Integer userId, Integer videoId){
        LambdaQueryWrapper<CollectGroup> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(CollectGroup::getUserId,userId);
        //获取当前用户的收藏夹列表
        List<CollectGroup> collectGroupList=collectGroupMapper.selectList(wrapper);
        if(collectGroupList.size()>0){
            List<Integer> ids=new ArrayList<>();
            Map<Integer, CollectGroupVO> map=new HashMap<>();
            //获取收藏夹列表的id集合用于查询收藏记录，并封装成最后的响应类的一部分
            for(CollectGroup collectGroup : collectGroupList){
                ids.add(collectGroup.getId());
                map.put(collectGroup.getId(),new CollectGroupVO().setCollectGroupId(collectGroup.getId()).setCollectGroupName(collectGroup.getName()).setCreateTime(collectGroup.getCreateTime()));
            }
            LambdaQueryWrapper<Collect> collectLambdaQueryWrapper=new LambdaQueryWrapper<>();
            collectLambdaQueryWrapper.in(Collect::getCollectGroupId,ids);
            collectLambdaQueryWrapper.eq(Collect::getVideoId,videoId);
            List<Collect> collects=collectMapper.selectList(collectLambdaQueryWrapper);
            //设置收藏夹的状态，可能有些收藏夹并没有收藏该视频则为默认值false
            for(Collect collect : collects){
                if(map.get(collect.getCollectGroupId())!=null){
                    map.get(collect.getCollectGroupId()).setHasCollect(true);
                }
            }
            //遍历收藏夹map并封装成集合
            List<CollectGroupVO> collectGroupVOList =new ArrayList<>();
            for(Map.Entry<Integer, CollectGroupVO> entry : map.entrySet()){
                collectGroupVOList.add(entry.getValue());
            }
            return Result.data(collectGroupVOList);
        }
        return Result.data(new ArrayList<>());
    }
    /**
     *编辑收藏夹
     */
    @Override
    public Result<Boolean> editCollectGroup(CollectGroupDTO createCollectGroupDTO) {
        if(createCollectGroupDTO.getId()!=null){
            collectGroupMapper.updateById(createCollectGroupDTO.toEntity());
        }else {
            collectGroupMapper.insert(createCollectGroupDTO.toEntity());

        }

        return Result.success(true);
    }
    /**
     *删除收藏夹
     */
    @Override
    public Result<Boolean> deleteCollectGroup(CollectGroupDTO createCollectGroupDTO) {
        collectGroupMapper.deleteById(createCollectGroupDTO.toEntity());
        return Result.success(true);
    }
    /**
     *获取首页收藏夹列表
     */
    @Override
    public Result<List<CollectGroup>> getCollectGroup(Integer userId) {
        LambdaQueryWrapper<CollectGroup> collectGroupQueryWrapper = new LambdaQueryWrapper<>();
        collectGroupQueryWrapper.eq(CollectGroup::getUserId, userId);
        collectGroupQueryWrapper.orderByDesc(CollectGroup::getCreateTime);
        return Result.data(collectGroupMapper.selectList(collectGroupQueryWrapper));
    }
    /**
     *获取某个收藏夹下的视频
     */
    @Override
    public Result<List<CollectVideoVO>> getCollectVideo(Integer collectGroupId) {
        return Result.data(videoServiceMapper.getCollectVideo(collectGroupId));
    }
}
