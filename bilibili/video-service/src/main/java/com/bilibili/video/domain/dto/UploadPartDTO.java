package com.bilibili.video.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadPartDTO {
    MultipartFile file;
    Integer resumableTotalChunks;
    String resumableIdentifier;
    Integer resumableChunkNumber;
}
