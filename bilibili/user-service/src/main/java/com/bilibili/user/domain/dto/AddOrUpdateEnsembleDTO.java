package com.bilibili.user.domain.dto;

import com.bilibili.common.domain.entity.user.VideoEnsemble;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AddOrUpdateEnsembleDTO {
    public Integer userId;
    public Integer id;
    public String name;
    public String intro;

    public VideoEnsemble toEntity(){
        VideoEnsemble videoEnsemble=new VideoEnsemble();
        BeanUtils.copyProperties(this,videoEnsemble);
        return videoEnsemble;
    }
}
