package com.bilibili.video.domain.vo;

import com.bilibili.common.domain.pojo.RecommendVideo;
import com.bilibili.common.serializer.RemoveTSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data

public class CommendVideoVO {

    Integer videoId;

    Integer authorId;

    String videoName;

    String authorName;
    String intro;
    @JsonSerialize(using = RemoveTSerializer.class)
    LocalDateTime createTime;
    String url;
    String length;
    String cover;

    Integer playCount;

    Integer danmakuCount;

    Integer collectCount;
    public CommendVideoVO(RecommendVideo recommendVideo){
        BeanUtils.copyProperties(recommendVideo,this);
    }
}
