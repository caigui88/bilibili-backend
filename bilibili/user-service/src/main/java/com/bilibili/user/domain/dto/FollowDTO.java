package com.bilibili.user.domain.dto;
import com.bilibili.common.domain.entity.user.Follow;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class FollowDTO {
    int fansId;
    int idolId;
    public Follow toEntity(){
        Follow follow=new Follow();
        BeanUtils.copyProperties(this,follow);
        return follow;
    }
}
