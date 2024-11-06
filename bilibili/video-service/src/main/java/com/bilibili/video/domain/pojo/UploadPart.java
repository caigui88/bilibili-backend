package com.bilibili.video.domain.pojo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class UploadPart {
    Map<Integer,String> partMap=new HashMap<>();
    Integer totalCount=0;
    Boolean hasCutImg=false;
    String cover="";
}
