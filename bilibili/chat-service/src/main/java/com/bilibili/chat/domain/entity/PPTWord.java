package com.bilibili.chat.domain.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
/**
 *沿用讯飞星火ppt详情类
 */
public class PPTWord {
    private String ThemeName;
    private String text;
    private String index;
}
