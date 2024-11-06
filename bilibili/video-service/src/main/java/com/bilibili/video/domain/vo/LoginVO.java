package com.bilibili.video.domain.vo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginVO {
    private Integer userId;
    private Boolean status;

}