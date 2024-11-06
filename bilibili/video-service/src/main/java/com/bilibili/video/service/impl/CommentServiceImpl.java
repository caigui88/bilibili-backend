package com.bilibili.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bilibili.api.client.SendNoticeClient;
import com.bilibili.common.domain.entity.user.IdCount;
import com.bilibili.common.domain.entity.video.audience_reactions.Comment;
import com.bilibili.common.domain.entity.video.audience_reactions.Like;
import com.bilibili.common.domain.entity.video.video_production.Video;
import com.bilibili.common.domain.entity.video.video_production.VideoData;
import com.bilibili.common.mapper.video.audience_reactions.CommentMapper;
import com.bilibili.common.mapper.video.audience_reactions.LikeMapper;
import com.bilibili.common.mapper.video.video_production.VideoDataMapper;
import com.bilibili.common.mapper.video.video_production.VideoMapper;
import com.bilibili.common.util.Result;
import com.bilibili.video.domain.dto.CommentDTO;
import com.bilibili.video.domain.vo.CommentDetailVO;
import com.bilibili.video.domain.vo.CommentVO;
import com.bilibili.video.domain.vo.TopCommentVO;
import com.bilibili.video.domain.vo.VideoCommentVO;
import com.bilibili.video.mapper.VideoServiceMapper;
import com.bilibili.video.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 *评论
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    CommentMapper commentMapper;
    @Resource
    LikeMapper likeMapper;
    @Resource
    SendNoticeClient client;
    @Resource
    VideoMapper videoMapper;
    @Resource
    VideoDataMapper videoDataMapper;
    @Resource
    VideoServiceMapper videoServiceMapper;
    /**
     *发送评论
     */
    @Override
    public Result<Boolean> commentToComment(CommentDTO commentDTO) {
        Comment comment = commentDTO.toEntity();
        commentMapper.insert(comment);
        LambdaQueryWrapper<Video> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Video::getId,comment.getVideoId());
        //只有该评论不是自己对自己发送的才会生成评论通知
        if(!videoMapper.selectOne(wrapper).getUserId().equals(commentDTO.getUserId())){
            client.sendCommentNotice(commentDTO.toNotice().setSenderId(comment.getId()));
        }
        return Result.success(true);
    }
    /**
     *获取评论
     */
    @Override
    public Result<VideoCommentVO> getComment(Integer videoId, Integer userId, Integer type) {
        LambdaQueryWrapper<VideoData> commentCountWrapper = new LambdaQueryWrapper<>();
        commentCountWrapper.eq(VideoData::getVideoId,videoId);
        //查询对视频的评论和对评论的评论并合并到一起
        List<CommentDetailVO> commentToVideoResponses = videoServiceMapper.getCommentToVideo(videoId);
        List<CommentDetailVO> commentToCommentResponses= videoServiceMapper.getCommentToComment(videoId);
        commentToVideoResponses.addAll(commentToCommentResponses);
        //如果评论数大于0
        if (commentToVideoResponses.size() > 0) {
            Set<Integer> commentIds = commentToVideoResponses.stream()
                    .map(CommentDetailVO::getId)
                    .collect(Collectors.toSet());
            //查询点赞记录并封装到对应的评论
            LambdaQueryWrapper<Like> likeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            likeLambdaQueryWrapper.in(Like::getCommentId, commentIds);
            likeLambdaQueryWrapper.eq(Like::getVideoId, videoId);
            likeLambdaQueryWrapper.eq(Like::getUserId, userId);
            List<Like> likes = likeMapper.selectList(likeLambdaQueryWrapper);
            Map<Integer, Boolean> likeMap = likes.stream()
                    .collect(Collectors.toMap(Like::getCommentId, like -> true, (existing, replacement) -> existing));
            commentToVideoResponses.forEach(response -> {
                response.setIsLiked(likeMap.getOrDefault(response.getId(), false));
            });
            List<IdCount> ids= commentMapper.getCommentCount(commentIds);
            Map<Integer, Integer> idCount=new HashMap<>(10);
            for(IdCount id : ids){
                idCount.put(id.getId(),id.getCount());
            }
            commentToVideoResponses.forEach(response -> {
                response.setLikeCount(idCount.getOrDefault(response.getId(), 0));
            });
        }
        //排序方式，根据值设置按点赞排序还是按创建时间排序，默认创建时间排序
        if(type==1){
            commentToVideoResponses.sort(Comparator.comparingInt(CommentDetailVO::getLikeCount).reversed());
//            Collections.sort(responses, (p1, p2) -> p2.getLikeCount() - p1.getLikeCount());
        }
        long count= videoDataMapper.selectOne(commentCountWrapper).getCommentCount();
        Map<Integer, CommentVO> responseMap=new HashMap<>();
        for(CommentDetailVO response : commentToVideoResponses){
            //如果是对视频的评论，即topId为null，封装更多内容
            if(response.getTopId()==null){
                CommentVO commentVO =new CommentVO();
                commentVO.setTopCommentVO(new TopCommentVO().setCreateTime(response.getCreateTime()).setId(response.getId()).setContent(response.getContent()).setSenderId(response.getSenderId()).setLikeCount(response.getLikeCount()).setIsLiked(response.getIsLiked()).setSenderName(response.getSenderName()).setSenderCoverUrl(response.getSenderCoverUrl()));
                responseMap.put(response.getId(), commentVO);
            }
            //如果是对评论的评论
            else{
                CommentVO commentVO =responseMap.getOrDefault(response.getTopId(),new CommentVO());
                commentVO.getCommentDetailRespons().add(response);
            }
        }
        List<CommentVO> commentVOList =new ArrayList<>();
        for(Map.Entry<Integer, CommentVO> responseEntry : responseMap.entrySet()){
            commentVOList.add(responseEntry.getValue());
        }
        return Result.data(new VideoCommentVO().setCommentVOList(commentVOList).setCommentCount(count
        ));
    }
}
