package com.bilibili.video.domain.dto;

import com.bilibili.common.domain.entity.notice.Dynamic;
import com.bilibili.common.domain.entity.user.User;
import com.bilibili.common.domain.entity.video.video_production.Video;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UploadVideoDTO {
    @ApiModelProperty("视频路径")
    private String url;
    @ApiModelProperty("视频名称")
    private String name;
    @ApiModelProperty("视频介绍")
    private String intro;
    @ApiModelProperty("作者id")
    private int userId;
    @ApiModelProperty("视频封面")
    private String videoCover;
    public Video toEntity() {
        Video video = new Video();
        BeanUtils.copyProperties(this, video);
        return video;
    }


    public Dynamic toNoCoverDynamic(User user, Video video){
        return new Dynamic().setVideoId(video.getId()).setVideoName(video.getName())
                .setAuthorId(user.getId());
    }
    public Dynamic toCoverDynamic(User user, Video video){
        return new Dynamic().setVideoId(video.getId()).setVideoCover(video.getCover()).setVideoName(video.getName())
                .setAuthorId(user.getId());
    }
}
