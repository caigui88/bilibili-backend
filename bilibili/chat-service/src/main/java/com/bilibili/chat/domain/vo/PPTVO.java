package com.bilibili.chat.domain.vo;

import com.bilibili.chat.domain.entity.PPTWord;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PPTVO {
    private String coverImgSrc;
    private List<PPTWord> pptWordList;
}
